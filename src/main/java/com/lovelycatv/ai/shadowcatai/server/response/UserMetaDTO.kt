package com.lovelycatv.ai.shadowcatai.server.response

/**
 * @author lovelycat
 * @since 2024-08-07 16:32
 * @version 1.0
 */
data class UserMetaDTO(
    var userId: Long?,
    var username: String?,
    var email: String?,
    var nickname: String?,
    var avatar: String?
)