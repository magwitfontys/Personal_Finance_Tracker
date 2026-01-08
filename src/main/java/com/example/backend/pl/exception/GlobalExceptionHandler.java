package com.example.backend.pl.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

/**
 * Global exception handler for the application.
 * Handles common exceptions and returns generic error messages to clients
 * while logging detailed information server-side.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles validation errors from @Valid annotations.
     * Returns a generic error message with an error reference.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        String errorId = UUID.randomUUID().toString();
        logger.warn("[{}] Validation error: {}", errorId, ex.getBindingResult().getAllErrors());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Request validation failed.", errorId));
    }

    /**
     * Handles registration exceptions.
     * Returns the specific error message to the client (e.g., "Username already exists").
     */
    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<?> handleRegistrationException(RegistrationException ex) {
        logger.warn("Registration error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new SimpleErrorResponse(ex.getMessage()));
    }

    /**
     * Handles unauthorized/authentication exceptions.
     * Returns the original error message (e.g., "Invalid credentials") with 401 status.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedException ex) {
        logger.warn("Unauthorized access attempt: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new SimpleErrorResponse(ex.getMessage()));
    }

    /**
     * Handles entity not found exceptions.
     * Returns a generic error message with an error reference.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        String errorId = UUID.randomUUID().toString();
        logger.warn("[{}] Entity not found: {}", errorId, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("The requested resource was not found.", errorId));
    }

    /**
     * Handles illegal argument exceptions (e.g., invalid input).
     * Returns a generic error message with an error reference.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        String errorId = UUID.randomUUID().toString();
        logger.warn("[{}] Illegal argument: {}", errorId, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Invalid request data.", errorId));
    }

    /**
     * Handles all other runtime exceptions.
     * Returns a generic error message with an error reference.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        String errorId = UUID.randomUUID().toString();
        logger.error("[{}] Unexpected error: ", errorId, ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Request failed.", errorId));
    }

    /**
     * Error response body sent to clients.
     * Contains a generic message and unique error reference for debugging.
     */
    static class ErrorResponse {
        public final String error;
        public final String errorId;

        ErrorResponse(String error, String errorId) {
            this.error = error;
            this.errorId = errorId;
        }
    }

    /**
     * Simple error response with just an error message (for authentication errors).
     */
    static class SimpleErrorResponse {
        public final String error;

        SimpleErrorResponse(String error) {
            this.error = error;
        }
    }
}
