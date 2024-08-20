package com.lovelycatv.ai.shadowcatai.server.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-01 18:42
 */
@TableName("users")
class UserEntity(
    @TableId(type = IdType.AUTO)
    var id: Long? = null,
    private var username: String? = null,
    private var password: String? = null,
    var email: String? = null,
    var nickname: String? = null,
    var phoneRegion: Int? = null,
    var phone: String? = null,
    var active: Boolean? = null,
    var registeredTime: Long? = null,
    var avatar: String? = null
) : UserDetails {

    @TableField(exist = false)
    private val authorities: Collection<GrantedAuthority> = ArrayList()
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    override fun getPassword(): String? = this.password


    override fun getUsername(): String? = this.username
}
