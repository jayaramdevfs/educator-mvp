package com.educator.auth;

import com.educator.auth.dto.JwtResponse;
import com.educator.auth.dto.LoginRequest;
import com.educator.auth.dto.PasswordResetConfirmRequest;
import com.educator.auth.dto.PasswordResetRequest;
import com.educator.auth.dto.PasswordResetRequestResponse;
import com.educator.auth.dto.RefreshTokenRequest;
import com.educator.auth.dto.RegisterRequest;
import com.educator.security.JwtUtil;
import com.educator.users.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final PasswordResetService passwordResetService;

    public AuthController(
            AuthService authService,
            JwtUtil jwtUtil,
            PasswordResetService passwordResetService
    ) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.passwordResetService = passwordResetService;
    }

    /**
     * Register new user (STUDENT by default)
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        authService.register(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Login user and return JWT
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {

        User user = authService.authenticate(
                request.getEmail(),
                request.getPassword()
        );

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        if (!jwtUtil.validateToken(request.getToken())) {
            throw new IllegalArgumentException("Invalid token");
        }

        String email = jwtUtil.extractEmail(request.getToken());
        User user = authService.getUserByEmail(email);
        String newToken = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new JwtResponse(newToken));
    }

    @PostMapping("/reset-request")
    public ResponseEntity<PasswordResetRequestResponse> requestPasswordReset(
            @Valid @RequestBody PasswordResetRequest request
    ) {
        String token = passwordResetService.createResetToken(request.getEmail());
        return ResponseEntity.ok(
                new PasswordResetRequestResponse(
                        "If the account exists, a reset token has been generated.",
                        token
                )
        );
    }

    @PostMapping("/reset-confirm")
    public ResponseEntity<?> confirmPasswordReset(
            @Valid @RequestBody PasswordResetConfirmRequest request
    ) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully");
    }
}
