package com.lovelycatv.ai.shadowcatai.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class ShadowCatAiServerApplicationTests {
    @Test
    public void testPasswordEncoder() {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}
