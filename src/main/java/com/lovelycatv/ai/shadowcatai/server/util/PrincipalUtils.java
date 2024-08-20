package com.lovelycatv.ai.shadowcatai.server.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

/**
 * Tools for decode token from requests
 *
 * @author lovelycat
 * @version 1.0
 * @since 2024-07-07 21:50
 */
@Component
public class PrincipalUtils {
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token, "jwtSignKey");
        return Long.valueOf(((Integer) claims.get("uid")));
    }

    public Claims getClaimsFromToken(String token, String signKey) {
        return Jwts.parser().setSigningKey(signKey).parseClaimsJws(token.replace("Bearer", "")).getBody();
    }
}
