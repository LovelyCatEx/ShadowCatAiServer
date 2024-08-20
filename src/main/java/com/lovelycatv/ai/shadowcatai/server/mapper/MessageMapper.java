package com.lovelycatv.ai.shadowcatai.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lovelycatv.ai.shadowcatai.server.entity.MessageEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-03 21:56
 */
@Mapper
public interface MessageMapper extends BaseMapper<MessageEntity> {
    @Insert("INSERT INTO " +
            "messages ( uid, session_id, assistant, message_type, message, datetime, depends, recalled )  " +
            "VALUES (  #{uid}, #{sessionId}, #{assistant}, #{messageType}, #{message}, #{datetime}, #{depends}, #{recalled}  )")
    void insertInto(MessageEntity messageEntity);
}
