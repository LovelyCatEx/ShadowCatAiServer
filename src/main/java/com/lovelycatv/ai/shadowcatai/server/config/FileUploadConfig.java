package com.lovelycatv.ai.shadowcatai.server.config;

import com.lovelycatv.ai.shadowcatai.server.config.custom.ShadowCatConfiguration;
import jakarta.annotation.Resource;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class FileUploadConfig {
    @Resource
    private ShadowCatConfiguration shadowCatConfiguration;

    /**
     * File upload configuration
     * 
     * @return MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofBytes(Integer.parseInt(shadowCatConfiguration.maxFileSize)));
        factory.setMaxRequestSize(DataSize.ofBytes(Integer.parseInt(shadowCatConfiguration.maxRequestSize)));
        return factory.createMultipartConfig();
    }
}