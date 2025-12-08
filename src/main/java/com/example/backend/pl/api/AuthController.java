package com.example.backend.pl.api;

import com.example.backend.bll.dto.LoginRequest;
import com.example.backend.bll.dto.RegisterRequest;
import com.example.backend.bll.dto.UserDTO;
import com.example.backend.bll.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "/api/auth", produces = "application/json")
@CrossOrigin(origins = {
        "http://localhost:5173", "http://127.0.0.1:5173",
        "http://localhost:4173", "http://127.0.0.1:4173"
})
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping(path = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        // Basic guard in case DTO validation is not present
        if (isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorBody("Username and password are required."));
        }

        try {
            UserDTO created = auth.register(request.getUsername().trim(), request.getPassword());
            // Location header points to the new user resource (adjust if your users route
            // differs)
            return ResponseEntity
                    .created(URI.create("/api/users/" + created.getId()))
                    .body(created);
        } catch (IllegalArgumentException ex) {
            // e.g., username already taken
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorBody(ex.getMessage()));
        }
    }

    @PostMapping(path = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        if (isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorBody("Username and password are required."));
        }

        UserDTO user = auth.authenticate(request.getUsername().trim(), request.getPassword());
        if (user == null) {
            // Front-end will show this message and stay on the login page
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorBody("Invalid credentials"));
        }

        // Return success with userId so frontend can store it
        return ResponseEntity.ok(new LoginSuccessBody(true, user.getId()));
    }

    /**
     * DELETE /api/auth/delete-account
     * Deletes the user account after password verification.
     * Expected JSON body:
     * {
     *   "userId": 123,
     *   "password": "user_password"
     * }
     */
    @DeleteMapping(path = "/delete-account", consumes = "application/json")
    public ResponseEntity<?> deleteAccount(@RequestBody DeleteAccountRequest request) {
        if (request.getUserId() == null || isBlank(request.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorBody("userId and password are required."));
        }

        try {
            boolean deleted = auth.deleteAccount(request.getUserId(), request.getPassword());
            if (!deleted) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorBody("Invalid password"));
            }
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorBody("Failed to delete account: " + ex.getMessage()));
        }
    }

    // --- Helpers & tiny JSON bodies so the front-end never breaks on empty
    // responses ---
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    static class ErrorBody {
        public final String error;

        ErrorBody(String error) {
            this.error = error;
        }
    }

    static class LoginSuccessBody {
        public final boolean success;
        public final Integer userId;

        LoginSuccessBody(boolean success, Integer userId) {
            this.success = success;
            this.userId = userId;
        }
    }

    static class DeleteAccountRequest {
        private Integer userId;
        private String password;

        public DeleteAccountRequest() {}

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
