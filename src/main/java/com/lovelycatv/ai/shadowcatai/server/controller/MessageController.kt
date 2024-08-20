package com.lovelycatv.ai.shadowcatai.server.controller

import com.lovelycatv.ai.shadowcatai.server.response.Result
import com.lovelycatv.ai.shadowcatai.server.service.MessageService
import com.lovelycatv.ai.shadowcatai.server.util.PrincipalUtils
import jakarta.annotation.Resource
import org.springframework.web.bind.annotation.*

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-05 22:32
 */
@RestController
@RequestMapping("/message")
open class MessageController {
    @Resource
    private lateinit var principalUtils: PrincipalUtils
    @Resource
    private lateinit var messageService: MessageService

    @GetMapping("/session")
    open fun getMessages(
        @RequestHeader("Authorization") token: String,
        @RequestParam("sessionId") sessionId: String,
        @RequestParam("datetime") datetime: Long?,
        @RequestParam("direction") direction: Boolean
    ): Result<*> {
        return Result.success(messageService.getMessagePageBySessionId(
            sessionId,
            principalUtils.getUserIdFromToken(token),
            true,
            1,
            16,
            if (datetime == null || datetime.toInt() == 0) null else datetime,
            direction
        ).records)
    }

    @PostMapping("/withdraw")
    open fun withdrawMessage(
        @RequestHeader("Authorization") token: String,
        @RequestParam("sessionId") sessionId: String,
        @RequestParam("messageId") messageId: Long
    ): Result<*> {
        messageService.withdrawMessage(principalUtils.getUserIdFromToken(token), sessionId, messageId)
        return Result.success()
    }
}
