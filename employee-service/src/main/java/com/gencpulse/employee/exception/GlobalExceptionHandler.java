package com.gencpulse.employee.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(
            ResourceNotFoundException ex) {

        return new ResponseEntity<>(
                Map.of(
                        "status", "ERROR",
                        "message", ex.getMessage(),
                        "timestamp", LocalDateTime.now().toString()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(
            Exception ex) {

        return new ResponseEntity<>(
                Map.of(
                        "status", "ERROR",
                        "message", ex.getMessage(),
                        "timestamp", LocalDateTime.now().toString()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return new ResponseEntity<>(
                Map.of(
                        "status", "ERROR",
                        "message", errorMessage,
                        "timestamp", LocalDateTime.now().toString()
                ),
                HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicate(
            DuplicateResourceException ex) {

        return new ResponseEntity<>(
                Map.of(
                        "status", "ERROR",
                        "message", ex.getMessage(),
                        "timestamp", LocalDateTime.now().toString()
                ),
                HttpStatus.CONFLICT
        );
    }

}