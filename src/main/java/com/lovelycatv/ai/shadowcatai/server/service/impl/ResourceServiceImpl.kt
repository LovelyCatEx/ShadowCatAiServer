package com.lovelycatv.ai.shadowcatai.server.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lovelycatv.ai.shadowcatai.server.config.custom.ShadowCatConfiguration
import com.lovelycatv.ai.shadowcatai.server.entity.ResourceEntity
import com.lovelycatv.ai.shadowcatai.server.mapper.ResourceMapper
import com.lovelycatv.ai.shadowcatai.server.service.ResourceService
import jakarta.annotation.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.UUID

/**
 * @author lovelycat
 * @since 2024-08-17 13:57
 * @version 1.0
 */
@Service
open class ResourceServiceImpl : ResourceService, ServiceImpl<ResourceMapper, ResourceEntity>() {
    @Resource
    private lateinit var resourceMapper: ResourceMapper
    @Resource
    private lateinit var shadowCatConfiguration: ShadowCatConfiguration

    override fun saveFile(uid: Long, file: MultipartFile): String {
        val fileName = UUID.randomUUID().toString()
        val extension = file.originalFilename!!.split(".").run { this.last() }
        val filePath = "${fileName}.${extension}"
        resourceMapper.insertResource(ResourceEntity(null, uid, extension, fileName, System.currentTimeMillis()))
        file.transferTo(File(shadowCatConfiguration.uploadPath + filePath))
        return filePath
    }

}