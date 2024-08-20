package com.lovelycatv.ai.shadowcatai.server.filter.auth;

import com.lovelycatv.ai.shadowcatai.server.response.Result;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

/**
 * All request should be checked by this filter
 *
 * @author lovelycat
 * @version 1.0
 * @since 2024-07-05 19:25
 */
public class AuthorizationFilter extends GenericFilterBean {
    private final Iterable<RequestMatcher> permitAllMatchers;

    public AuthorizationFilter(Iterable<RequestMatcher> permitAllMatchers) {
        this.permitAllMatchers = permitAllMatchers;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 此过滤链在 FilterSecurityInterceptor 之前 因此检查 Token 前需要先排除已放行的路径
        for (RequestMatcher matcher : permitAllMatchers) {
            if (matcher.matches(request)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String tokenStr = request.getHeader("Authorization");
        try {
            if (tokenStr == null) {
                throw new IllegalStateException("Token invalid");
            }
            Claims claims = Jwts.parser()
                    // 必须与 CustomLoginFilter 中的 key 保持一致
                    .setSigningKey("jwtSignKey")
                    .parseClaimsJws(tokenStr.replace("Bearer",""))
                    .getBody();
            String username = claims.getSubject();
            List<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
            filterChain.doFilter(request, servletResponse);
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException | IllegalStateException e) {
            // 当 Token 无法被解析时抛出 SignatureException 异常
            // JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.

            // 当 Token 格式不正确时抛出 MalformedJwtException 异常
            // JWT strings must contain exactly 2 period characters. Found: 0

            // e.printStackTrace();
            servletResponse.setContentType("application/json;charset=utf-8");
            // 将请求失败的信息写回给接口
            servletResponse.getWriter().write(Result.failed(Result.CODE_ERR_BAD_REQUEST, "Token invalid").toJSONString());
            servletResponse.getWriter().flush();
            servletResponse.getWriter().close();
        }
    }
}
