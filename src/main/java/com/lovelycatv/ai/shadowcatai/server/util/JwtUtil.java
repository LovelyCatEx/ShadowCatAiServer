package com.lovelycatv.ai.shadowcatai.server.util;

import com.lovelycatv.ai.shadowcatai.server.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-07-05 18:44
 */
public final class JwtUtil {
    private JwtUtil() {

    }

    /**
     * Build Jwt Token
     * @param authorities Authorities of user
     * @param authentication Authentication
     * @param expiration Token Expiration (ms)
     * @return Token String
     */
    public static <T extends UserEntity> String buildJwtToken(String signKey, Collection<? extends GrantedAuthority> authorities, Authentication authentication, long expiration) {
        StringBuilder authorityStr = new StringBuilder();
        for (GrantedAuthority authority : authorities) {
            authorityStr.append(authority).append(",");
        }

        return Jwts.builder()
                .claim("authorities", authorityStr)
                .claim("uid", ((T) authentication.getPrincipal()).getId())
                .setSubject(authentication.getName())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, signKey)
                .compact();
    }
}
