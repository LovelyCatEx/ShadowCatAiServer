package com.lovelycatv.ai.shadowcatai.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lovelycatv.ai.shadowcatai.server.entity.MessageEntity;
import com.lovelycatv.ai.shadowcatai.server.mapper.MessageMapper;
import com.lovelycatv.ai.shadowcatai.server.util.RedisOperator;

import java.util.List;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-03 21:56
 */
public interface MessageService extends IService<MessageEntity> {
    MessageMapper getMessageMapper();

    List<MessageEntity> getMessagesBySessionId(String sessionId, boolean excludeRecalled, boolean cacheFirst);
    List<MessageEntity> getMessagesBySessionId(String sessionId, Long userId, boolean excludeRecalled, boolean cacheFirst);

    IPage<MessageEntity> getMessagePageBySessionId(
            String sessionId,
            Long userId,
            boolean excludeRecalled,
            int page,
            int size,
            Long datetime,
            boolean beforeOrAfter
    );

    Long addMessage(MessageEntity message);

    void deleteBySessionId(String sessionId);

    void withdrawMessage(Long userId, String sessionId, Long messageId);

    String getSessionMessagesCacheKey(String sessionId);

    RedisOperator<String> getMessageListOperator();
}
