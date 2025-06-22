package com.example.ws_cert.constant;

import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
public enum ApiSuccessCode {
    SUCCESS(99, "Success", HttpStatus.SC_OK),
    CREATED(98, "Created", HttpStatus.SC_CREATED),
    ACCEPTED(97, "Accepted", HttpStatus.SC_ACCEPTED),
    ;

    private final int code;
    private final String message;
    private final int httpStatus;
    ApiSuccessCode(int code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
