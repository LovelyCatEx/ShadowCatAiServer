package com.lovelycatv.ai.shadowcatai.server.filter.auth;

import com.lovelycatv.ai.shadowcatai.server.entity.UserEntity;
import com.lovelycatv.ai.shadowcatai.server.response.Result;
import com.lovelycatv.ai.shadowcatai.server.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Custom login filter
 * @author lovelycat
 * @version 1.0
 * @since 2024-07-05 04:55
 */
public class CustomLoginFilter extends AbstractAuthenticationProcessingFilter {

    public CustomLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl, authenticationManager);
    }

    /**
     * User authentication
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return Authentication
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    /**
     * Authorization successful
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param chain FilterChain
     * @param authResult Authentication
     * @throws IOException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();

        response.setContentType("application/json;charset=utf-8");
        String userToken = JwtUtil.buildJwtToken("jwtSignKey", authorities, authResult, 60 * 60 * 1000);
        Map<String, String> map = new HashMap<>();
        UserEntity principal = (UserEntity) authResult.getPrincipal();
        map.put("uid", Objects.requireNonNull(principal.getId()).toString());
        map.put("token", userToken);
        response.getWriter().write(Result.success(map).toJSONString());
        response.getWriter().flush();
        response.getWriter().close();
        logger.info("Authentication successful: " + principal.getUsername());
    }

    /**
     * Authorization failed
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param failed AuthenticationException
     * @throws IOException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(Result.failed(Result.CODE_ERR_NOT_AUTHORIZED, "Username or password incorrect").toJSONString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
