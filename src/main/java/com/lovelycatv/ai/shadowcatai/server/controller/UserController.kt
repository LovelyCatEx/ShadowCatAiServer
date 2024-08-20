package com.lovelycatv.ai.shadowcatai.server.controller

import com.lovelycatv.ai.shadowcatai.server.response.Result
import com.lovelycatv.ai.shadowcatai.server.response.UserMetaDTO
import com.lovelycatv.ai.shadowcatai.server.service.ResourceService
import com.lovelycatv.ai.shadowcatai.server.service.UserService
import com.lovelycatv.ai.shadowcatai.server.util.PrincipalUtils
import jakarta.annotation.Resource
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/**
 * @author lovelycat
 * @since 2024-08-06 19:42
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
open class UserController {
    @Resource
    private lateinit var principalUtils: PrincipalUtils
    @Resource
    private lateinit var userService: UserService
    @Resource
    private lateinit var resourceService: ResourceService

    @GetMapping("/meta")
    fun getMetaData(@RequestHeader("Authorization") authorization: String): Result<*> {
        val userEntity = userService.getByUserId(principalUtils.getUserIdFromToken(authorization))
        return Result.success(UserMetaDTO(
            userEntity.id,
            userEntity.username,
            userEntity.email,
            userEntity.nickname,
            userEntity.avatar
        ))
    }

    @PostMapping("/avatar")
    fun uploadResource(
        @RequestHeader("Authorization") token: String,
        file: MultipartFile
    ): Result<*> {
        val uid = principalUtils.getUserIdFromToken(token)
        val avatar = resourceService.saveFile(uid, file)
        userService.updateAvatar(uid, avatar)
        return Result.success("", avatar)
    }

    @PostMapping("/nickname")
    fun updateNickname(
        @RequestHeader("Authorization") token: String,
        @RequestParam("nickname") nickname: String
    ): Result<*> {
        userService.updateNickname(principalUtils.getUserIdFromToken(token), nickname)
        return Result.success()
    }
}