package com.lovelycatv.ai.shadowcatai.server.entity

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

/**
 * @author lovelycat
 * @since 2024-08-17 13:55
 * @version 1.0
 */
@TableName("resources")
class ResourceEntity(
    @TableId
    var id: Long?,
    var uid: Long?,
    var extension: String?,
    var fileName: String?,
    var timestamp: Long?
)