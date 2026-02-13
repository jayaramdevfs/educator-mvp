package com.educator.profile;

import com.educator.profile.dto.ProfileResponse;
import com.educator.users.User;
import com.educator.users.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(String email) {
        User user = getByEmailOrThrow(email);
        return mapToProfileResponse(user);
    }

    public ProfileResponse updateProfile(String authenticatedEmail, String email) {
        User user = getByEmailOrThrow(authenticatedEmail);

        String normalizedEmail = email.trim().toLowerCase();
        if (!user.getEmail().equalsIgnoreCase(normalizedEmail)
                && userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email already registered");
        }

        user.setEmail(normalizedEmail);
        User updated = userRepository.save(user);
        return mapToProfileResponse(updated);
    }

    public void changePassword(
            String authenticatedEmail,
            String currentPassword,
            String newPassword
    ) {
        User user = getByEmailOrThrow(authenticatedEmail);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private User getByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private ProfileResponse mapToProfileResponse(User user) {
        return new ProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getRoles()
                        .stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet())
        );
    }
}
