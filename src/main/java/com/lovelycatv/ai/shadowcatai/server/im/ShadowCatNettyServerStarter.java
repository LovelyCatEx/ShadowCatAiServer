package com.lovelycatv.ai.shadowcatai.server.im;

import com.lovelycatv.ai.shadowcatai.server.config.custom.ShadowCatConfiguration;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-05 01:37
 */
@Component
public class ShadowCatNettyServerStarter implements CommandLineRunner {
    @Resource
    private ShadowCatNettyServer shadowCatNettyServer;
    @Resource
    private ShadowCatConfiguration shadowCatConfiguration;

    @Override
    public void run(String... args) {
        shadowCatNettyServer.start(shadowCatConfiguration.chatPort);
    }
}
