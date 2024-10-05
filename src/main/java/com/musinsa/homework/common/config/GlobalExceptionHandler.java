package com.musinsa.homework.common.config;

import com.musinsa.homework.common.exception.ApiErrorCode;
import com.musinsa.homework.common.exception.ApiException;
import com.musinsa.homework.controller.response.ApiResponseWrapper;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleApiCustomException(ApiException ex) {
        return ResponseEntity.status(ex.getApiErrorCode().getHttpStatus())
                .body(ApiResponseWrapper.error(ex.getApiErrorCode().getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleValidationException(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseWrapper.error(ApiErrorCode.BAD_REQUEST.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseWrapper.error(ApiErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseWrapper.error(ApiErrorCode.UNKNOWN.getMessage()));
    }
}