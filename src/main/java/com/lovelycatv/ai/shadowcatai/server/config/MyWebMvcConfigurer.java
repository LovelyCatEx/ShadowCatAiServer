package com.lovelycatv.ai.shadowcatai.server.config;

import com.lovelycatv.ai.shadowcatai.server.config.custom.ShadowCatConfiguration;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    @Resource
    private ShadowCatConfiguration shadowCatConfiguration;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("file:" + shadowCatConfiguration.uploadPath);
    }
}