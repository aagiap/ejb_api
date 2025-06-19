package com.example.ws_cert.exception;

import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.SC_UNAUTHORIZED),
    FORBIDDEN(403, "Forbidden", HttpStatus.SC_FORBIDDEN),
    NOT_FOUND(404, "Not Found", HttpStatus.SC_NOT_FOUND),
    BAD_REQUEST(400, "Bad Request", HttpStatus.SC_BAD_REQUEST),

    // 5xx - Server errors
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    SSL_HANDSHAKE_ERROR(500, "SSL Handshake Error", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE(503, "Service Unavailable", HttpStatus.SC_INTERNAL_SERVER_ERROR),

    // Custom application errors
    USER_ALREADY_EXISTS(1001, "User already exists", HttpStatus.SC_BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.SC_BAD_REQUEST),
    UNSUPPORTED_HTTP_METHOD(1006, "Unsupported HTTP method", HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED),
    FAIL_TO_LOAD_KEYSTORE_FROM_PATH(1007, "Failed to load keystore from path", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    KEYSTORE_NOT_FOUND(1008, "Keystore not found", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    FAILED_TO_SERIALIZE_BODY_TO_JSON(1009, "Failed to serialize body to JSON", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    FAILED_TO_GET_RESPONSE(1010, "Failed to get response", HttpStatus.SC_INTERNAL_SERVER_ERROR);

    ErrorCode(int code, String message, int httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    private final int code;
    private final String message;
    private final int httpStatusCode;
}
