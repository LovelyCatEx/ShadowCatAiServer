package com.lovelycatv.ai.shadowcatai.server.service

import com.baomidou.mybatisplus.extension.service.IService
import com.lovelycatv.ai.shadowcatai.server.entity.ResourceEntity
import org.springframework.web.multipart.MultipartFile

/**
 * @author lovelycat
 * @since 2024-08-17 13:57
 * @version 1.0
 */
interface ResourceService : IService<ResourceEntity> {
    fun saveFile(uid: Long, file: MultipartFile): String
}