package com.lovelycatv.ai.shadowcatai.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lovelycatv.ai.shadowcatai.server.entity.UserEntity;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-01 19:00
 */
public interface UserService extends IService<UserEntity> {
    UserEntity getByUserNameOrEmail(String text);

    UserEntity getByUserId(Long userId);

    void updateAvatar(Long uid, String avatar);

    void updateNickname(Long uid, String nickname);
}
