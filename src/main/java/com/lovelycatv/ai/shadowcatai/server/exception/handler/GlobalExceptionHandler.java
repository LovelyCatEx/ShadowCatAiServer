package com.lovelycatv.ai.shadowcatai.server.exception.handler;

import com.lovelycatv.ai.shadowcatai.server.exception.ModelNotFoundException;
import com.lovelycatv.ai.shadowcatai.server.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler
 *
 * @author lovelycat
 * @version 1.0
 * @since 2024-07-05 23:35
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> handleInvalidToken(ModelNotFoundException e) {
        // log.info("Token invalid", e);
        return Result.failed(Result.CODE_ERR_NOT_AUTHORIZED, e.getMessage());
    }
}
