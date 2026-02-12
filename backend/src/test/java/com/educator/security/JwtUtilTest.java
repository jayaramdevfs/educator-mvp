package com.educator.security;

import com.educator.roles.Role;
import com.educator.users.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private static final String TEST_SECRET =
            "educator-secret-key-change-later-which-is-long-enough-for-hs256-signing";

    @Test
    void generateAndValidateToken_success() {
        JwtUtil jwtUtil = new JwtUtil(TEST_SECRET, 60_000);
        User user = createUser("jwt-user@example.com", List.of(Role.STUDENT));

        String token = jwtUtil.generateToken(user);

        assertThat(token).isNotBlank();
        assertThat(jwtUtil.validateToken(token)).isTrue();
    }

    @Test
    void extractEmail_returnsSubjectFromToken() {
        JwtUtil jwtUtil = new JwtUtil(TEST_SECRET, 60_000);
        User user = createUser("email-check@example.com", List.of(Role.STUDENT));

        String token = jwtUtil.generateToken(user);

        assertThat(jwtUtil.extractEmail(token)).isEqualTo("email-check@example.com");
    }

    @Test
    void extractRoles_returnsRoleClaimsFromToken() {
        JwtUtil jwtUtil = new JwtUtil(TEST_SECRET, 60_000);
        User user = createUser("roles-check@example.com", List.of(Role.STUDENT, Role.ADMIN));

        String token = jwtUtil.generateToken(user);

        List<String> roles = jwtUtil.extractRoles(token);
        assertThat(roles).contains(Role.STUDENT, Role.ADMIN);
    }

    @Test
    void validateToken_returnsFalseForExpiredToken() {
        JwtUtil jwtUtil = new JwtUtil(TEST_SECRET, -1);
        User user = createUser("expired@example.com", List.of(Role.STUDENT));

        String token = jwtUtil.generateToken(user);

        assertThat(jwtUtil.validateToken(token)).isFalse();
    }

    @Test
    void validateToken_returnsFalseForTamperedToken() {
        JwtUtil jwtUtil = new JwtUtil(TEST_SECRET, 60_000);
        User user = createUser("tampered@example.com", List.of(Role.STUDENT));
        String token = jwtUtil.generateToken(user);
        String tampered = token.substring(0, token.length() - 2) + "aa";

        assertThat(jwtUtil.validateToken(tampered)).isFalse();
    }

    private User createUser(String email, List<String> roleNames) {
        User user = new User(email, "encoded-password");
        roleNames.stream().map(Role::new).forEach(user::addRole);
        return user;
    }
}
