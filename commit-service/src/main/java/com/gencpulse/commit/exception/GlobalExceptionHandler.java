package com.gencpulse.commit.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(
            RuntimeException ex) {

        Map<String,Object> response =
                new HashMap<>();

        response.put("status","ERROR");
        response.put("message",ex.getMessage());
        response.put("timestamp",
                LocalDateTime.now().toString());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String,Object> response =
                new HashMap<>();

        response.put("status","ERROR");

        response.put(
                "message",
                ex.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage());

        response.put(
                "timestamp",
                LocalDateTime.now().toString());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}