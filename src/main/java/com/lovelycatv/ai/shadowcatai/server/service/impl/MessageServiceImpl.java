package com.lovelycatv.ai.shadowcatai.server.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovelycatv.ai.shadowcatai.server.entity.MessageEntity;
import com.lovelycatv.ai.shadowcatai.server.mapper.MessageMapper;
import com.lovelycatv.ai.shadowcatai.server.service.MessageService;
import com.lovelycatv.ai.shadowcatai.server.util.GlobalConstants;
import com.lovelycatv.ai.shadowcatai.server.util.RedisOperator;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-03 21:57
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageEntity> implements MessageService {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private RedisOperator<String> messageListOperator;

    @Override
    public MessageMapper getMessageMapper() {
        return this.messageMapper;
    }

    @Override
    public List<MessageEntity> getMessagesBySessionId(String sessionId, boolean excludeRecalled, boolean cacheFirst) {
        return getMessagesBySessionId(sessionId, null, excludeRecalled, true);
    }

    @Override
    public List<MessageEntity> getMessagesBySessionId(String sessionId, Long userId, boolean excludeRecalled, boolean cacheFirst) {
        List<MessageEntity> list;
        String cacheKey = getSessionMessagesCacheKey(sessionId);
        if (messageListOperator.hasKey(cacheKey) && cacheFirst) {
            list = MessageEntity.decodeListFromCache(messageListOperator.get(cacheKey));
        } else {
            LambdaQueryWrapper<MessageEntity> queryWrapper = new LambdaQueryWrapper<MessageEntity>()
                    .eq(MessageEntity::getSessionId, sessionId);
            if (userId != null) {
                queryWrapper.and(it -> it.eq(MessageEntity::getUid, userId));
            }
            if (excludeRecalled) {
                queryWrapper.and(it -> it.eq(MessageEntity::getRecalled, !excludeRecalled));
            }
            queryWrapper.orderByAsc(MessageEntity::getDatetime);
            list = new ArrayList<>(list(queryWrapper));
            messageListOperator.set(cacheKey, MessageEntity.encodeListForCache(list));
        }
        return list;
    }

    @Override
    public IPage<MessageEntity> getMessagePageBySessionId(String sessionId, Long userId, boolean excludeRecalled, int page, int size, Long datetime, boolean beforeOrAfter) {
        if (datetime == null || datetime == 0L) {
            datetime = System.currentTimeMillis();
        }

        LambdaQueryWrapper<MessageEntity> queryWrapper = new LambdaQueryWrapper<MessageEntity>()
                .eq(MessageEntity::getSessionId, sessionId)
                .and(it -> it.eq(MessageEntity::getUid, userId));
        if (excludeRecalled) {
            queryWrapper.and(it -> it.eq(MessageEntity::getRecalled, !excludeRecalled));
        }

        Long finalDatetime = datetime;

        if (beforeOrAfter) {
            queryWrapper.and(it -> it.lt(MessageEntity::getDatetime, finalDatetime));
            queryWrapper.orderByDesc(MessageEntity::getDatetime);
        } else {
            queryWrapper.and(it -> it.gt(MessageEntity::getDatetime, finalDatetime));
            queryWrapper.orderByAsc(MessageEntity::getDatetime);
        }



        return messageMapper.selectPage(new Page<>(), queryWrapper);
    }


    @Override
    public Long addMessage(MessageEntity message) {
        String cacheKey = getSessionMessagesCacheKey(message.getSessionId());
        List<MessageEntity> messages;

        if (messageListOperator.hasKey(cacheKey)) {
            messages = MessageEntity.decodeListFromCache(messageListOperator.get(cacheKey));
        } else {
            messages = getMessagesBySessionId(message.getSessionId(), true, false);
        }

        messageMapper.insert(message);

        // Fix: adding new message with null messageId to cache before its id is returned by database
        // After insert, the message object will knows its messageId
        messages.add(message);

        messageListOperator.set(cacheKey, MessageEntity.encodeListForCache(messages));
        return message.getId();
    }

    @Override
    public void deleteBySessionId(String sessionId) {
        messageMapper.delete(new LambdaQueryWrapper<MessageEntity>().eq(MessageEntity::getSessionId, sessionId));
    }

    @Override
    public void withdrawMessage(Long userId, String sessionId, Long messageId) {
        MessageEntity messageEntity = getById(messageId);
        System.out.println("Withdrawing message: " + JSON.toJSONString(messageEntity));

        if (messageEntity == null) {
            return;
        }

        if (Objects.requireNonNull(messageEntity.getUid()).longValue() != userId.longValue() || !Objects.equals(messageEntity.getSessionId(), sessionId)) {
            return;
        }

        messageEntity.setRecalled(true);
        updateById(messageEntity);

        // Update the corresponding cache
        String cacheKey = getSessionMessagesCacheKey(sessionId);
        if (messageListOperator.hasKey(cacheKey)) {
            List<MessageEntity> modifiedMessages = MessageEntity.decodeListFromCache(messageListOperator.get(cacheKey));
            modifiedMessages = modifiedMessages.stream()
                    .filter(it -> Objects.requireNonNull(it.getId()).longValue() != messageId)
                    .toList();
            messageListOperator.set(cacheKey, MessageEntity.encodeListForCache(modifiedMessages));
        }
    }

    @Override
    public String getSessionMessagesCacheKey(String sessionId) {
        return GlobalConstants.CACHE_KEY_MESSAGES_IN_SESSION.formatted(sessionId);
    }

    @Override
    public RedisOperator<String> getMessageListOperator() {
        return this.messageListOperator;
    }
}
