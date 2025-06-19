package com.example.ws_cert.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;
}
