package com.lovelycatv.ai.shadowcatai.server.config;

import com.lovelycatv.ai.shadowcatai.server.filter.auth.AuthorizationFilter;
import com.lovelycatv.ai.shadowcatai.server.filter.auth.CustomLoginFilter;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-01 18:12
 */
@Configuration
public class SecurityConfig {
    @Resource
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // Unrestricted apis
        List<RequestMatcher> requestMatchers = new ArrayList<>();
        requestMatchers.add(new AntPathRequestMatcher("/login", HttpMethod.POST.name()));
        requestMatchers.add(new AntPathRequestMatcher("/assets/**"));

        http.authorizeHttpRequests(registry -> {
            requestMatchers.forEach(e -> registry.requestMatchers(e).permitAll());
            registry.anyRequest().authenticated();
        });

        http.exceptionHandling(e -> e.accessDeniedHandler(new CustomAccessDeniedHandler()));

        http.sessionManagement(it -> it.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Disable BasicAuth
        http.httpBasic(AbstractHttpConfigurer::disable);

        // Disable CSRF
        http.csrf(AbstractHttpConfigurer::disable);

        // Disable Cors
        http.cors(AbstractHttpConfigurer::disable);

        http.addFilterBefore(
                new CustomLoginFilter("/login", authenticationConfiguration.getAuthenticationManager()),
                UsernamePasswordAuthenticationFilter.class
        );

        http.addFilterBefore(new AuthorizationFilter(requestMatchers), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 由 AuthenticationConfiguration 得到 AuthenticationManager
     * @param authenticationConfiguration AuthenticationConfiguration
     * @return AuthenticationManager
     */
    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 使用 BCryptPasswordEncoder 作为密码加密方式
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
