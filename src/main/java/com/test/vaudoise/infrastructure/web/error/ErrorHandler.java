package com.test.vaudoise.infrastructure.web.error;

import com.test.vaudoise.core.exception.NotFoundException;
import com.test.vaudoise.core.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> domainValidation(ValidationException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", ex.getMessage()
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", ex.getMessage()
        ));
    }

    @ExceptionHandler(com.fasterxml.jackson.databind.exc.InvalidFormatException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFormat(com.fasterxml.jackson.databind.exc.InvalidFormatException ex) {
        if (ex.getTargetType().equals(LocalDate.class)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid date format, expected ISO 8601 (yyyy-MM-dd)"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", ex.getOriginalMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> Map.of(
                        "field", err.getField(),
                        "message", formatMessage(err)
                ))
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation failed");
        response.put("details", fieldErrors);
        response.put("count", fieldErrors.size());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String,Object>> notFound(NotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    private String formatMessage(FieldError err) {
        String msg = err.getDefaultMessage();
        if (msg == null) return "Invalid value";

        return switch (msg) {
            case "must not be blank" -> "is required";
            case "must not be null" -> "is required";
            case "must be a well-formed email address" -> "is not a valid email";
            default -> msg;
        };
    }
}