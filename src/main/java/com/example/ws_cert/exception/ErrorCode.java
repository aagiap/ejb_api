package com.example.ws_cert.exception;

import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_EXISTED(1, "User not existed", HttpStatus.SC_NOT_FOUND),
    USER_ALREADY_EXISTS(2, "User already exists", HttpStatus.SC_BAD_REQUEST),
    USER_NOT_FOUND(3, "User not found", HttpStatus.SC_BAD_REQUEST),
    UNAUTHENTICATED(4, "Unauthenticated", HttpStatus.SC_BAD_REQUEST),
    UNAUTHORIZED(5, "Unauthorized", HttpStatus.SC_UNAUTHORIZED),
    USER_PASSWORD_IS_NULL(6, "User password is null", HttpStatus.SC_BAD_REQUEST),
    ROLE_NOT_FOUND(7,"Role not found" , HttpStatus.SC_NOT_FOUND),
    DATA_INTEGRITY_VIOLATION(8, "Execute SQL statement fails to map the given data", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    WRONG_PASSWORD(9, "Wrong password", HttpStatus.SC_UNAUTHORIZED),
    VALIDATION_FAILED(10, "Validation failed", HttpStatus.SC_BAD_REQUEST),
    FAILED_TO_GET_RESPONSE(11, "Failed to get response from other application", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    FAILED_TO_CREATE_SSL_CONTEXT_WITH_EJBCA(12, "Failed to create SSL context with EJBCA", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    FAILED_TO_LOAD_KEYSTORE(13, "Failed to load keystore", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    FAILED_TO_LOAD_KEY_STORE_FROM_FILE(14, "Failed to load keystore from file", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    FAILED_TO_READ_CRL_FILE(15, "Failed to read CRL file", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    UNSUPPORTED_HTTP_METHOD(16, "Unsupported HTTP method", HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED),
    KEYSTORE_NOT_FOUND(17, "Keystore not found", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    FAILED_TO_SERIALIZE_BODY_TO_JSON(18, "Failed to serialize body to JSON", HttpStatus.SC_INTERNAL_SERVER_ERROR),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error", HttpStatus.SC_INTERNAL_SERVER_ERROR),

    ;

    ErrorCode(int code, String message, int httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    private final int code;
    private final String message;
    private final int httpStatusCode;
}
