package com.lovelycatv.ai.shadowcatai.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovelycatv.ai.shadowcatai.server.entity.MessageEntity;
import com.lovelycatv.ai.shadowcatai.server.entity.ModelEntity;
import com.lovelycatv.ai.shadowcatai.server.entity.SessionEntity;
import com.lovelycatv.ai.shadowcatai.server.exception.ModelNotFoundException;
import com.lovelycatv.ai.shadowcatai.server.mapper.SessionMapper;
import com.lovelycatv.ai.shadowcatai.server.service.MessageService;
import com.lovelycatv.ai.shadowcatai.server.service.ModelService;
import com.lovelycatv.ai.shadowcatai.server.service.SessionService;
import com.lovelycatv.ai.shadowcatai.server.util.GlobalConstants;
import com.lovelycatv.ai.shadowcatai.server.util.RedisOperator;
import jakarta.annotation.Resource;
import org.apache.shardingsphere.sharding.algorithm.keygen.SnowflakeKeyGenerateAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-05 17:46
 */
@Service
public class SessionServiceImpl extends ServiceImpl<SessionMapper, SessionEntity> implements SessionService {
    @Resource
    private SessionMapper sessionMapper;
    @Resource
    private ModelService modelService;
    @Resource
    private MessageService messageService;
    @Resource
    private RedisOperator<SessionEntity> sessionEntityOperator;

    @Override
    public String createSession(String name, Long uid, Long modelId) {
        ModelEntity modelEntity = modelService.getById(modelId);
        if (modelEntity == null) {
            throw new ModelNotFoundException("Model %s not found".formatted(modelId));
        }

        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId(UUID.randomUUID().toString().replace("-", ""));
        sessionEntity.setName(name);
        sessionEntity.setUid(uid);
        sessionEntity.setModelId(modelId);

        sessionEntityOperator.set(GlobalConstants.CACHE_KEY_SESSION.formatted(sessionEntity.getId()), sessionEntity);
        sessionMapper.insert(sessionEntity);

        return sessionEntity.getId();
    }

    @Override
    public Iterable<SessionEntity> getSessionsByUserId(Long userId) {
        return list(new LambdaQueryWrapper<SessionEntity>().eq(SessionEntity::getUid, userId));
    }

    @Override
    public SessionEntity getBySessionId(String sessionId) {
        String cacheKey = GlobalConstants.CACHE_KEY_SESSION.formatted(sessionId);
        if (sessionEntityOperator.hasKey(cacheKey)) {
            return sessionEntityOperator.get(cacheKey);
        } else {
            SessionEntity session = getOne(new LambdaQueryWrapper<SessionEntity>().eq(SessionEntity::getId, sessionId));
            if (session != null) {
                sessionEntityOperator.set(cacheKey, session);
                return session;
            } else {
                return null;
            }
        }
    }

    @Override
    public SessionEntity getDetailedBySessionId(String sessionId) {
        SessionEntity session = getBySessionId(sessionId);
        if (session != null) {
            if (session.getModelEntity() == null) {
                String cacheKey = GlobalConstants.CACHE_KEY_SESSION.formatted(sessionId);
                session.setModelEntity(modelService.getById(session.getModelId()));
                sessionEntityOperator.set(cacheKey, session);
            }
            return session;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteSession(Long uid, String sessionId, boolean deleteMessages) {
        LambdaQueryWrapper<SessionEntity> queryWrapper = new LambdaQueryWrapper<SessionEntity>()
                .eq(SessionEntity::getUid, uid)
                .and(it -> it.eq(SessionEntity::getId, sessionId));
        if (!deleteMessages) {
            sessionMapper.delete(queryWrapper);
        } else {
            // Verify that the session is belongs to the user
            SessionEntity sessionEntity = sessionMapper.selectOne(queryWrapper);
            if (sessionEntity != null) {
                sessionMapper.deleteById(sessionEntity);
                messageService.deleteBySessionId(sessionEntity.getId());
            }
        }
    }

    @Transactional
    @Override
    public void createBranch(String originalSessionId, Long before, String name, Long uid, Long modelId) {
        List<MessageEntity> originalMessages = messageService.getMessagesBySessionId(originalSessionId, uid, true, false);
        originalMessages = originalMessages.stream()
                .filter(it -> it.getDatetime() != null && it.getDatetime() <= before)
                .toList();

        String newSessionId = createSession(name, uid, modelId);

        // New messages list
        List<MessageEntity> newMessages = new ArrayList<>();

        // Original map New
        Map<Long, Long> newUserMessageIds = new HashMap<>();
        originalMessages.stream()
                .filter(it -> !it.getAssistant())
                .peek(it -> it.setSessionId(newSessionId))
                .forEach((message) -> {
                    if (!message.getAssistant()) {
                        Long originalMessageId = message.getId();

                        SnowflakeKeyGenerateAlgorithm algorithm = new SnowflakeKeyGenerateAlgorithm();
                        message.setId((Long) algorithm.generateKey());
                        messageService.getMessageMapper().insertInto(message);

                        newUserMessageIds.put(originalMessageId, message.getId());
                        newMessages.add(message);
                    }
                });

        originalMessages.stream()
                .filter(MessageEntity::getAssistant)
                .peek((it) -> {
                    it.setSessionId(newSessionId);
                    it.setDepends(newUserMessageIds.get(it.getDepends()));
                })
                .forEach((message) -> {
                    messageService.getMessageMapper().insertInto(message);
                    newMessages.add(message);
                });

        newMessages.sort(Comparator.comparing(MessageEntity::getDatetime));

        String cacheKey = messageService.getSessionMessagesCacheKey(newSessionId);
        messageService.getMessageListOperator().set(cacheKey, MessageEntity.encodeListForCache(newMessages));
    }

    @Override
    public void updateSession(Long uid, String sessionId, String sessionName, Long modelId) {
        LambdaUpdateWrapper<SessionEntity> updateWrapper = new LambdaUpdateWrapper<SessionEntity>()
                .eq(SessionEntity::getUid, uid)
                .and((it) -> it.eq(SessionEntity::getId, sessionId))
                .set(SessionEntity::getName, sessionName)
                .set(SessionEntity::getModelId, modelId);
        sessionMapper.update(updateWrapper);

        String cacheKey = GlobalConstants.CACHE_KEY_SESSION.formatted(sessionId);
        sessionEntityOperator.set(cacheKey, new SessionEntity(sessionId, uid, modelId, sessionName));
    }
}
