package com.example.backend.pl.api;

import com.example.backend.bll.dto.LoginRequest;
import com.example.backend.bll.dto.RegisterRequest;
import com.example.backend.bll.dto.UserDTO;
import com.example.backend.bll.service.AuthService;
import com.example.backend.pl.exception.EntityNotFoundException;
import com.example.backend.pl.exception.UnauthorizedException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping(path = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        // Basic guard in case DTO validation is not present
        if (isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            throw new IllegalArgumentException("Username and password are required.");
        }

        UserDTO created = auth.register(request.getUsername().trim(), request.getPassword());
        // Location header points to the new user resource (adjust if your users route differs)
        return ResponseEntity
                .created(URI.create("/api/users/" + created.getId()))
                .body(created);
    }

    @PostMapping(path = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String username = request.getUsername() != null ? request.getUsername().trim() : null;
        
        if (isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            logger.warn("action=LOGIN, username={}, result=FAILURE, reason=missing_credentials", username);
            throw new IllegalArgumentException("Username and password are required.");
        }

        UserDTO user = auth.authenticate(username, request.getPassword());
        if (user == null) {
            logger.warn("action=LOGIN, username={}, result=FAILURE, reason=invalid_credentials", username);
            throw new UnauthorizedException("Invalid credentials");
        }

        logger.info("action=LOGIN, username={}, userId={}, result=SUCCESS", username, user.getId());
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
            logger.warn("action=DELETE_ACCOUNT, userId={}, result=FAILURE, reason=missing_fields", request.getUserId());
            throw new IllegalArgumentException("userId and password are required.");
        }

        boolean deleted = auth.deleteAccount(request.getUserId(), request.getPassword());
        if (!deleted) {
            logger.warn("action=DELETE_ACCOUNT, userId={}, result=FAILURE, reason=invalid_password", request.getUserId());
            throw new EntityNotFoundException("Invalid password");
        }
        
        logger.info("action=DELETE_ACCOUNT, userId={}, result=SUCCESS", request.getUserId());
        return ResponseEntity.noContent().build();
    }

    // --- Helpers & tiny JSON bodies so the front-end never breaks on empty
    // responses ---
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
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
