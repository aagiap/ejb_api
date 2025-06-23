package com.example.ws_cert.exception;

import com.example.ws_cert.dto.response.ApiResponse;
//import jakarta.validation.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<?>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setStatus(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .status(errorCode.getCode())
                        .message(ex.getMessage() != null ? ex.getMessage() : "Unexpected error")
                        .build());
    }


    //  Validation - @Valid DTO (POST/PUT request body)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setStatus(ErrorCode.VALIDATION_FAILED.getCode());
        apiResponse.setMessage(ErrorCode.VALIDATION_FAILED.getMessage());

        apiResponse.setResponse(message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponse);
    }

    //  Validation - PathVariable, RequestParam, Service (@Validated)
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException ex) {
//
//        String message = ex.getConstraintViolations()
//                .stream()
//                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
//                .collect(Collectors.joining("; "));
//
//        ApiResponse<String> apiResponse = new ApiResponse<>();
//
//        apiResponse.setStatus(ErrorCode.VALIDATION_FAILED.getCode());
//        apiResponse.setMessage(ErrorCode.VALIDATION_FAILED.getMessage());
//        apiResponse.setResponse(message);
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(apiResponse);
//    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleIntegrity(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildError(ErrorCode.CONSTRAINT_VIOLATION, ex);
        } else if (ex.getCause() instanceof PropertyValueException) {
            return buildError(ErrorCode.PROPERTY_VALUE_EXCEPTION, ex.getCause());
        } else if (ex.getCause() instanceof DataException) {
            return buildError(ErrorCode.DATA_EXCEPTION, ex.getCause());
        }
        return buildError(ErrorCode.DATA_INTEGRITY_VIOLATION, ex);
    }

    private ResponseEntity<ApiResponse<?>> buildError(ErrorCode errorCode, Throwable root) {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage() + ": " + (root != null ? root.getMessage() : "Unknown error"));
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }


//    @ExceptionHandler(InternalAuthenticationServiceException.class)
//    public ResponseEntity<ApiResponse<?>> handleInternalAuthException(InternalAuthenticationServiceException ex) {
//        if (ex.getCause() instanceof AppException appEx) {
//            return handlingAppException(appEx);
//        }
//
//        return handleGeneralException(ex);
//    }


}
