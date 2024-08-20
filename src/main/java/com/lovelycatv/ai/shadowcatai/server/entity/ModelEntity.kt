package com.lovelycatv.ai.shadowcatai.server.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-03 21:54
 */
@TableName("models")
class ModelEntity(
    @TableId(type = IdType.AUTO)
    var id: Long? = null,
    var name: String? = null,
    var description: String? = null,
    var qualifiedName: String? = null,
    var available: Boolean? = null,
    var supportImage: Boolean? = null,
    var supportVideo: Boolean? = null,
    var supportAudio: Boolean? = null
)
