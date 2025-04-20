package com.test.ecommerce.common;


import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.atomic.AtomicReference;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> userException(GlobalException e) {
        return ApiResponse.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        AtomicReference<String> errors = new AtomicReference<>();
        e.getBindingResult().getAllErrors().forEach(error -> errors.set(error.getDefaultMessage()));
        return ApiResponse.fail(e.getStatusCode().toString(), String.valueOf(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> serverException(Exception e) {
        e.printStackTrace();
        return ApiResponse.serverException("SERVER_ERROR", e.getMessage());
    }
}
