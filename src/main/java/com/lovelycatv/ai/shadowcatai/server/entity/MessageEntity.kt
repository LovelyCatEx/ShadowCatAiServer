package com.lovelycatv.ai.shadowcatai.server.entity

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-03 21:48
 */
@TableName("messages")
class MessageEntity(
    @TableId
    var id: Long?,
    var uid: Long?,
    var sessionId: String?,
    var assistant: Boolean = false,
    var messageType: Int?,
    var message: String?,
    var datetime: Long?,
    var depends: Long?,
    var recalled: Boolean = false
) {
    companion object {
        @JvmStatic
        fun encodeListForCache(messages: Iterable<MessageEntity?>): String {
            val result = JSONObject()

            var userId = 0L
            val msgArr = JSONArray()
            messages.filterNotNull().forEach {
                val t = mutableListOf<String>()
                userId = it.uid ?: 0L
                t.add(it.id.toString())
                t.add(it.assistant.toString())
                t.add(it.messageType.toString())
                t.add(it.message ?: "")
                t.add(it.depends.toString())
                msgArr.add(t)
            }

            result["uid"] = userId
            result["m"] = msgArr

            return JSON.toJSONString(result)
        }

        @JvmStatic
        fun decodeListFromCache(str: String): List<MessageEntity> {
            val result = mutableListOf<MessageEntity>()
            val obj = JSONObject.parse(str)
            val userId = obj.getLong("uid")
            with(obj.getJSONArray("m")) {
                for (i in 0 until this.size) {
                    with(this.getJSONArray(i)) {
                        result.add(MessageEntity(
                            this.getLong(0),
                            userId,
                            "",
                            this.getBoolean(1),
                            this.getInteger(2),
                            this.getString(3),
                            0,
                            this.getLong(4)
                        ))
                    }
                }
            }

            return result
        }
    }
}
