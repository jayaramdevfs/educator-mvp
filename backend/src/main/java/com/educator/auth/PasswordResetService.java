package com.educator.auth;

import com.educator.users.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final int expiryMinutes;

    private final Map<String, PasswordResetToken> tokens = new ConcurrentHashMap<>();

    public PasswordResetService(
            UserRepository userRepository,
            AuthService authService,
            @Value("${app.security.password-reset.expiry-minutes:15}") int expiryMinutes
    ) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.expiryMinutes = expiryMinutes;
    }

    public String createResetToken(String email) {
        String normalizedEmail = email.trim().toLowerCase();
        if (!userRepository.existsByEmail(normalizedEmail)) {
            return null;
        }

        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(expiryMinutes, ChronoUnit.MINUTES);
        tokens.put(token, new PasswordResetToken(normalizedEmail, expiresAt));
        return token;
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokens.get(token);
        if (resetToken == null) {
            throw new IllegalArgumentException("Invalid reset token");
        }

        if (Instant.now().isAfter(resetToken.expiresAt())) {
            tokens.remove(token);
            throw new IllegalArgumentException("Reset token has expired");
        }

        authService.resetPassword(resetToken.email(), newPassword);
        tokens.remove(token);
    }

    private record PasswordResetToken(String email, Instant expiresAt) {
    }
}
