package com.lovelycatv.ai.shadowcatai.server.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.lovelycatv.ai.shadowcatai.server.entity.ResourceEntity
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper

/**
 * @author lovelycat
 * @since 2024-08-17 13:57
 * @version 1.0
 */
@Mapper
interface ResourceMapper : BaseMapper<ResourceEntity> {
    @Insert("INSERT INTO resources (uid, extension, file_name, timestamp) VALUES (#{uid}, #{extension}, #{fileName}, #{timestamp})")
    fun insertResource(entity: ResourceEntity)
}