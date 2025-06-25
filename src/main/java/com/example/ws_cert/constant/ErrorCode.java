package com.example.ws_cert.constant;

import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_ALREADY_EXISTS(1, "User already exists", HttpStatus.SC_BAD_REQUEST),
    USER_NOT_FOUND(2, "User not found", HttpStatus.SC_BAD_REQUEST),
    UNAUTHENTICATED(3, "Unauthenticated", HttpStatus.SC_BAD_REQUEST),
    UNAUTHORIZED(4, "Unauthorized", HttpStatus.SC_UNAUTHORIZED),
    ROLE_NOT_FOUND(5,"Role not found" , HttpStatus.SC_NOT_FOUND),
    DATA_INTEGRITY_VIOLATION(6, "There are error performing database operations", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    WRONG_PASSWORD(7, "Wrong password", HttpStatus.SC_UNAUTHORIZED),
    VALIDATION_FAILED(8, "Validation failed", HttpStatus.SC_BAD_REQUEST),
    FAILED_TO_GET_RESPONSE(9, "Failed to get response from other application", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    FAILED_TO_CREATE_SSL_CONTEXT_WITH_EJBCA(10, "Failed to create SSL context with EJBCA", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    FAILED_TO_LOAD_KEYSTORE(11, "Failed to load keystore", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    FAILED_TO_LOAD_KEY_STORE_FROM_FILE(12, "Failed to load keystore from file", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    FAILED_TO_READ_CRL_FILE(13, "Failed to read CRL file", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    UNSUPPORTED_HTTP_METHOD(14, "Unsupported HTTP method", HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED),
    KEYSTORE_NOT_FOUND(15, "Keystore not found", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    FAILED_TO_SERIALIZE_BODY_TO_JSON(16, "Failed to serialize body to JSON", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    CONSTRAINT_VIOLATION(17, "Database integrity constraint violation", HttpStatus.SC_BAD_REQUEST),
    PROPERTY_VALUE_EXCEPTION(18, "Entity being persisted with a problem.", HttpStatus.SC_BAD_REQUEST),
    DATA_EXCEPTION(19, "Something was wrong with the SQL statement or the data, in that particular context. ", HttpStatus.SC_BAD_REQUEST),


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
