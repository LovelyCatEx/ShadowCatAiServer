package com.lovelycatv.ai.shadowcatai.server.config.custom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-17 19:28
 */
@Configuration
@ConfigurationProperties("shadowcat")
public class ShadowCatConfiguration {
    @Value("${shadowcat.uploads.path}")
    public String uploadPath;

    @Value("${shadowcat.uploads.maxFileSize}")
    public String maxFileSize;
    @Value("${shadowcat.uploads.maxRequestSize}")
    public String maxRequestSize;

    @Value("${server.chat-port}")
    public int chatPort;

    @Value("${shadowcat.init.username}")
    public String initUsername;
    @Value("${shadowcat.init.password}")
    public String initPassword;
    @Value("${shadowcat.init.email}")
    public String initEmail;
}
