package com.example.weatherinsight.exception.handler;

import com.example.weatherinsight.dto.ApiResponseDto;
import com.example.weatherinsight.exception.custom.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice(basePackages = "com.example.weatherinsight.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponseDto<String>> handleAppException(ApplicationException e) {
        return ResponseEntity.status(e.getHttpStatusCode()).body(ApiResponseDto.error(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDto<String>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(ApiResponseDto.error(e.getMessage()));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<String>> handleException(Exception e) {
        return ResponseEntity.internalServerError().body(ApiResponseDto.error("Exception Unexpected: ", e.getMessage()));

    }
}
