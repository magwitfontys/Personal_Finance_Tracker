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
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UserDTO created = auth.register(request.getUsername(), request.getPassword());
            return ResponseEntity
                    .created(URI.create("/api/users/" + created.getId()))
                    .body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorBody(ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        boolean ok = auth.login(request.getUsername(), request.getPassword());
        if (!ok) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorBody("Invalid credentials"));
        }
        // return simple JSON so the frontend’s res.json() succeeds
        return ResponseEntity.ok(new SuccessBody(true));
    }

    // --- tiny JSON bodies so frontend never breaks on empty responses ---
    static class ErrorBody {
        public final String error;

        ErrorBody(String error) {
            this.error = error;
        }
    }

    static class SuccessBody {
        public final boolean success;

        SuccessBody(boolean success) {
            this.success = success;
        }
    }
}
