package com.project.movie_reservation_system.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.movie_reservation_system.exception.CustomException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String,String>> handleCustomException(CustomException customException)
    {
        return ResponseEntity
                .status(customException.getHttpStatus())
                .body(Map.of("message",customException.getMessage()));
    }
}
