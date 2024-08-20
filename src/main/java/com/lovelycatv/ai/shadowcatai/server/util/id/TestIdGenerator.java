package com.lovelycatv.ai.shadowcatai.server.util.id;

import org.apache.shardingsphere.sharding.spi.KeyGenerateAlgorithm;
import org.springframework.stereotype.Component;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-02 14:27
 */
@Component
public class TestIdGenerator implements KeyGenerateAlgorithm {
    @Override
    public Comparable<?> generateKey() {
        return 127L;
    }


    @Override
    public String getType() {
        return "CUSTOM";
    }

    @Override
    public void init() {

    }
}
