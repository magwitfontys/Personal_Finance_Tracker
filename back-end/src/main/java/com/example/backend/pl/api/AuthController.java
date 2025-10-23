package com.example.backend.pl.api;

import com.example.backend.bll.dto.*;
import com.example.backend.bll.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.status(HttpStatus.CREATED).body(auth.register(request));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Object(){ public final String error = ex.getMessage(); });
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResult> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(auth.login(request));
    }
}
