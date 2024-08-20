package com.lovelycatv.ai.shadowcatai.server

import com.lovelycatv.ai.shadowcatai.server.config.custom.ShadowCatConfiguration
import com.lovelycatv.ai.shadowcatai.server.entity.UserEntity
import com.lovelycatv.ai.shadowcatai.server.mapper.UserMapper
import jakarta.annotation.Resource
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

/**
 * @author lovelycat
 * @since 2024-08-20 23:53
 * @version 1.0
 */
@Component
class InitC : CommandLineRunner {
    @Resource
    private lateinit var shadowCatConfiguration: ShadowCatConfiguration
    @Resource
    private lateinit var userMapper: UserMapper
    @Resource
    private lateinit var passwordEncoder: PasswordEncoder

    override fun run(vararg args: String?) {
        val uuidFilePath = File("").absolutePath + "/uuid.data"
        if (File(uuidFilePath).exists()) {
            println("Initialized")
            return
        }
        userMapper.insert(UserEntity(
            null,
            shadowCatConfiguration.initUsername,
            passwordEncoder.encode(shadowCatConfiguration.initPassword),
            shadowCatConfiguration.initEmail,
            shadowCatConfiguration.initUsername,
            0,
            "",
            true,
            System.currentTimeMillis(),
            ""
        ))
        println("Initial user: ${shadowCatConfiguration.initUsername}")

        if (File(uuidFilePath).exists()) {
            val reader = BufferedReader(FileReader(uuidFilePath))
            ShadowCatAiServerApplication.SERVER_UUID = reader.readLine()
            reader.close()
        } else {
            ShadowCatAiServerApplication.SERVER_UUID = UUID.randomUUID().toString().replace("-", "")
            val myWriter = FileWriter(uuidFilePath)
            myWriter.write(ShadowCatAiServerApplication.SERVER_UUID)
            myWriter.close()
        }

        println("UUID: " + ShadowCatAiServerApplication.SERVER_UUID)
    }
}