package com.lovelycatv.ai.shadowcatai.server.response;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request response
 *
 * @param <T> Data Type
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERR_BAD_REQUEST = 400;
    public static final int CODE_ERR_NOT_AUTHORIZED = 401;
    public static final int CODE_ERR_FORBIDDEN = 403;
    public static final int CODE_ERR_INTERNAL_ERROR = 500;

    public static final int CODE_ACTION_LOGIN_INVALID = 601;

    public static final int CODE_SERVICE_ERR_FORBIDDEN = 40003;

    public static final String MESSAGE_SUCCESS_DEFAULT = "";

    public static final int CODE_AUTH_CONSENT_INVALID = 60000;

    public static Result<Void> success() {
        return new Result<>(CODE_SUCCESS, MESSAGE_SUCCESS_DEFAULT, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(CODE_SUCCESS, MESSAGE_SUCCESS_DEFAULT, data);
    }

    public static Result<Void> success(String message) {
        return new Result<>(CODE_SUCCESS, message, null);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(CODE_SUCCESS, message, data);
    }

    public static Result<Void> failed(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> failed(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public static Result<Void> unrecognizedPrincipal() {
        return Result.failed(Result.CODE_ERR_BAD_REQUEST, "Unrecognized principal");
    }

    public static Result<Void> invalidPermissionRequest() {
        return new Result<>(CODE_ERR_FORBIDDEN, "Permission denied", null);
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
