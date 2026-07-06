package com.finalproject.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // This method runs automatically whenever @Valid validation fails, ANYWHERE in your app
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        String firstError = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Invalid input");

        return ResponseEntity.status(400).body(Map.of("message", "Please fill out every box"));
    }

    // Also handles your manual business-logic exceptions from ServiceCreationManager
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<?> handleBusinessErrors(RuntimeException ex) {
        return ResponseEntity.status(400).body(Map.of("message", ex.getMessage()));
    }
}