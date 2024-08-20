package com.lovelycatv.ai.shadowcatai.server.config;

import com.lovelycatv.ai.shadowcatai.server.response.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-07-07 00:07
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(Result.failed(Result.CODE_ERR_FORBIDDEN, "Access denied").toJSONString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
