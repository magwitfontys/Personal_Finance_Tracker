package com.example.backend.pl.exception;

/**
 * Exception thrown during user registration when validation fails.
 * Unlike IllegalArgumentException, this exception returns the specific error message to the client.
 */
public class RegistrationException extends RuntimeException {
    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
