package com.lovelycatv.ai.shadowcatai.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.UUID;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.lovelycatv", "org.apache.shardingsphere"})
public class ShadowCatAiServerApplication {
    public static boolean isModelGenerating = false;
    public static String SERVER_UUID = "";

    public static void main(String[] args) {
        SpringApplication.run(ShadowCatAiServerApplication.class, args);
    }

}
