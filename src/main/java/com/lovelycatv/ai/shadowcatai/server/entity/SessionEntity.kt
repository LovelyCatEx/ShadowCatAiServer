package com.lovelycatv.ai.shadowcatai.server.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.NoArgsConstructor

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-05 17:44
 */
@NoArgsConstructor
@TableName("sessions")
class SessionEntity(
    @TableId(type = IdType.ASSIGN_UUID)
    var id: String? = null,
    var uid: Long? = null,
    var modelId: Long? = null,
    var name: String? = null
) {
    @TableField(exist = false)
    var modelEntity: ModelEntity? = null
}
