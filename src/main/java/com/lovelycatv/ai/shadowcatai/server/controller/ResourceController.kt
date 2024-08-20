package com.lovelycatv.ai.shadowcatai.server.controller

import com.lovelycatv.ai.shadowcatai.server.service.ResourceService
import jakarta.annotation.Resource
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author lovelycat
 * @since 2024-08-17 15:20
 * @version 1.0
 */
@RestController
@RequestMapping("/resource")
open class ResourceController {
    @Resource
    private lateinit var resourceService: ResourceService
}