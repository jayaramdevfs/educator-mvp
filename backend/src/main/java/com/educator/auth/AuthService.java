package com.educator.auth;

import com.educator.roles.Role;
import com.educator.roles.RoleRepository;
import com.educator.users.User;
import com.educator.users.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
public class AuthService {

    private static final Pattern STRONG_PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{8,}$");

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new user with STUDENT role
     */
    public void register(String email, String rawPassword) {
        String normalizedEmail = email.trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email already registered");
        }

        validateStrongPassword(rawPassword);

        User user = new User(
                normalizedEmail,
                passwordEncoder.encode(rawPassword)
        );

        Role studentRole = roleRepository.findByName(Role.STUDENT)
                .orElseThrow(() -> new IllegalStateException("STUDENT role not found"));

        user.addRole(studentRole);

        userRepository.save(user);
    }

    /**
     * Validate login credentials (JWT will be added later)
     */
    public User authenticate(String email, String rawPassword) {

        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return user;
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void resetPassword(String email, String newPassword) {
        validateStrongPassword(newPassword);
        User user = getUserByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void validateStrongPassword(String rawPassword) {
        if (rawPassword == null || !STRONG_PASSWORD_PATTERN.matcher(rawPassword).matches()) {
            throw new IllegalArgumentException(
                    "Password must be at least 8 characters and include at least one uppercase letter and one digit"
            );
        }
    }
}
