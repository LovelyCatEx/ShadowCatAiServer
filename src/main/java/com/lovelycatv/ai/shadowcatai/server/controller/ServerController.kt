package com.lovelycatv.ai.shadowcatai.server.controller

import com.lovelycatv.ai.shadowcatai.server.ShadowCatAiServerApplication
import com.lovelycatv.ai.shadowcatai.server.response.Result
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author lovelycat
 * @since 2024-08-06 19:52
 * @version 1.0
 */
@RestController
@RequestMapping("/server")
class ServerController {

    @GetMapping("/uuid")
    fun getUuid(): Result<*> {
        return Result.success("", ShadowCatAiServerApplication.SERVER_UUID)
    }
}