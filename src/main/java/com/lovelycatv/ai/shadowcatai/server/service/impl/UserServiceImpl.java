package com.lovelycatv.ai.shadowcatai.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovelycatv.ai.shadowcatai.server.entity.UserEntity;
import com.lovelycatv.ai.shadowcatai.server.mapper.UserMapper;
import com.lovelycatv.ai.shadowcatai.server.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-01 19:00
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService, UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = getByUserNameOrEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return userEntity;
    }

    @Override
    public UserEntity getByUserNameOrEmail(String text) {
        return getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, text).or().eq(UserEntity::getEmail, text));
    }

    @Override
    public UserEntity getByUserId(Long userId) {
        return getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getId, userId));
    }

    @Override
    public void updateAvatar(Long uid, String avatar) {
        update(new LambdaUpdateWrapper<UserEntity>().eq(UserEntity::getId, uid).set(UserEntity::getAvatar, avatar));
    }

    @Override
    public void updateNickname(Long uid, String nickname) {
        update(new LambdaUpdateWrapper<UserEntity>().eq(UserEntity::getId, uid).set(UserEntity::getNickname, nickname));
    }
}
