package com.educator.auth;

import com.educator.roles.Role;
import com.educator.roles.RoleRepository;
import com.educator.users.User;
import com.educator.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_savesEncodedUserWithStudentRole() {
        when(userRepository.existsByEmail("student@example.com")).thenReturn(false);
        when(passwordEncoder.encode("StrongPass1")).thenReturn("encoded-password");
        when(roleRepository.findByName(Role.STUDENT)).thenReturn(Optional.of(new Role(Role.STUDENT)));

        authService.register("student@example.com", "StrongPass1");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("student@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("encoded-password");
        assertThat(savedUser.hasRole(Role.STUDENT)).isTrue();
    }

    @Test
    void register_rejectsDuplicateEmail() {
        when(userRepository.existsByEmail("student@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register("student@example.com", "StrongPass1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already registered");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_rejectsWeakPassword() {
        when(userRepository.existsByEmail("student@example.com")).thenReturn(false);

        assertThatThrownBy(() -> authService.register("student@example.com", "weakpass"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Password must be at least 8 characters");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_failsWhenStudentRoleMissing() {
        when(userRepository.existsByEmail("student@example.com")).thenReturn(false);
        when(passwordEncoder.encode("StrongPass1")).thenReturn("encoded-password");
        when(roleRepository.findByName(Role.STUDENT)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.register("student@example.com", "StrongPass1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("STUDENT role not found");
    }

    @Test
    void authenticate_returnsUserForValidCredentials() {
        User user = new User("student@example.com", "encoded-password");
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("StrongPass1", "encoded-password")).thenReturn(true);

        User authenticated = authService.authenticate("student@example.com", "StrongPass1");

        assertThat(authenticated).isSameAs(user);
    }

    @Test
    void authenticate_rejectsUnknownEmail() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.authenticate("missing@example.com", "StrongPass1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    void authenticate_rejectsWrongPassword() {
        User user = new User("student@example.com", "encoded-password");
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("WrongPass1", "encoded-password")).thenReturn(false);

        assertThatThrownBy(() -> authService.authenticate("student@example.com", "WrongPass1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid credentials");
    }
}
