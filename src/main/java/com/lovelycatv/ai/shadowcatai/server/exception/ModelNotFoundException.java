package com.lovelycatv.ai.shadowcatai.server.exception;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-05 18:11
 */
public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(String message) {
        super(message);
    }
}
