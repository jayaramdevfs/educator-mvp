package com.educator.profile;

import com.educator.roles.Role;
import com.educator.users.User;
import com.educator.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void getProfile_returnsMappedUser() {
        User user = user(1L, "student@example.com", "encoded");
        user.addRole(new Role(Role.STUDENT));
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(user));

        var profile = profileService.getProfile("student@example.com");

        assertThat(profile.getId()).isEqualTo(1L);
        assertThat(profile.getEmail()).isEqualTo("student@example.com");
        assertThat(profile.getRoles()).containsExactly("STUDENT");
    }

    @Test
    void getProfile_throwsWhenUserMissing() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.getProfile("missing@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");
    }

    @Test
    void updateProfile_normalizesEmailAndSaves() {
        User user = user(1L, "old@example.com", "encoded");
        when(userRepository.findByEmail("old@example.com")).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var profile = profileService.updateProfile("old@example.com", "  NEW@EXAMPLE.COM ");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("new@example.com");
        assertThat(profile.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    void updateProfile_doesNotCheckDuplicateWhenEmailUnchangedIgnoringCase() {
        User user = user(1L, "student@example.com", "encoded");
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        profileService.updateProfile("student@example.com", "STUDENT@EXAMPLE.COM");

        verify(userRepository, never()).existsByEmail("student@example.com");
    }

    @Test
    void updateProfile_throwsWhenTargetEmailAlreadyExists() {
        User user = user(1L, "student@example.com", "encoded");
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThatThrownBy(() -> profileService.updateProfile("student@example.com", "taken@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already registered");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateProfile_throwsWhenAuthenticatedUserMissing() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.updateProfile("missing@example.com", "new@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");
    }

    @Test
    void changePassword_updatesWhenCurrentPasswordMatches() {
        User user = user(1L, "student@example.com", "old-encoded");
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("OldPass1", "old-encoded")).thenReturn(true);
        when(passwordEncoder.encode("NewPass1")).thenReturn("new-encoded");

        profileService.changePassword("student@example.com", "OldPass1", "NewPass1");

        assertThat(user.getPassword()).isEqualTo("new-encoded");
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_throwsWhenCurrentPasswordMismatch() {
        User user = user(1L, "student@example.com", "old-encoded");
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("WrongPass1", "old-encoded")).thenReturn(false);

        assertThatThrownBy(() -> profileService.changePassword("student@example.com", "WrongPass1", "NewPass1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Current password is incorrect");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changePassword_throwsWhenUserMissing() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.changePassword("missing@example.com", "OldPass1", "NewPass1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");
    }

    @Test
    void updateProfile_trimsEmailBeforeSaving() {
        User user = user(1L, "student@example.com", "encoded");
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("trimmed@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var profile = profileService.updateProfile("student@example.com", "  trimmed@example.com   ");

        assertThat(profile.getEmail()).isEqualTo("trimmed@example.com");
    }

    private static User user(Long id, String email, String password) {
        User user = new User(email, password);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}

