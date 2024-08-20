package com.lovelycatv.ai.shadowcatai.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lovelycatv.ai.shadowcatai.server.entity.SessionEntity;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-05 17:45
 */
public interface SessionService extends IService<SessionEntity> {
    String createSession(String name, Long uid, Long modelId);

    Iterable<SessionEntity> getSessionsByUserId(Long userId);

    SessionEntity getBySessionId(String sessionId);

    SessionEntity getDetailedBySessionId(String sessionId);

    void deleteSession(Long uid, String sessionId, boolean deleteMessages);

    void createBranch(String originalSessionId, Long before, String name, Long uid, Long modelId);

    void updateSession(Long uid, String sessionId, String sessionName, Long modelId);
}
