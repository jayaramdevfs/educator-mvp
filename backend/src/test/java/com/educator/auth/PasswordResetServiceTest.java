package com.educator.auth;

import com.educator.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @Test
    void createResetToken_returnsTokenWhenUserExists() {
        PasswordResetService service = new PasswordResetService(userRepository, authService, 15);
        when(userRepository.existsByEmail("student@example.com")).thenReturn(true);

        String token = service.createResetToken("student@example.com");

        assertThat(token).isNotBlank();
    }

    @Test
    void createResetToken_normalizesEmailBeforeLookup() {
        PasswordResetService service = new PasswordResetService(userRepository, authService, 15);
        when(userRepository.existsByEmail("student@example.com")).thenReturn(true);

        service.createResetToken("  STUDENT@EXAMPLE.COM ");

        verify(userRepository).existsByEmail("student@example.com");
    }

    @Test
    void createResetToken_returnsNullWhenUserDoesNotExist() {
        PasswordResetService service = new PasswordResetService(userRepository, authService, 15);
        when(userRepository.existsByEmail("missing@example.com")).thenReturn(false);

        String token = service.createResetToken("missing@example.com");

        assertThat(token).isNull();
    }

    @Test
    void createResetToken_generatesDistinctTokensForRepeatedRequests() {
        PasswordResetService service = new PasswordResetService(userRepository, authService, 15);
        when(userRepository.existsByEmail("student@example.com")).thenReturn(true);

        String first = service.createResetToken("student@example.com");
        String second = service.createResetToken("student@example.com");

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void resetPassword_delegatesToAuthServiceForValidToken() {
        PasswordResetService service = new PasswordResetService(userRepository, authService, 15);
        when(userRepository.existsByEmail("student@example.com")).thenReturn(true);
        String token = service.createResetToken("student@example.com");

        service.resetPassword(token, "NewStrong1");

        verify(authService).resetPassword("student@example.com", "NewStrong1");
    }

    @Test
    void resetPassword_throwsForUnknownToken() {
        PasswordResetService service = new PasswordResetService(userRepository, authService, 15);

        assertThatThrownBy(() -> service.resetPassword("unknown-token", "NewStrong1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid reset token");

        verifyNoInteractions(authService);
    }

    @Test
    void resetPassword_throwsWhenTokenExpired() {
        PasswordResetService service = new PasswordResetService(userRepository, authService, -1);
        when(userRepository.existsByEmail("student@example.com")).thenReturn(true);
        String token = service.createResetToken("student@example.com");

        assertThatThrownBy(() -> service.resetPassword(token, "NewStrong1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reset token has expired");

        verify(authService, never()).resetPassword("student@example.com", "NewStrong1");
    }

    @Test
    void resetPassword_removesTokenAfterSuccessfulReset() {
        PasswordResetService service = new PasswordResetService(userRepository, authService, 15);
        when(userRepository.existsByEmail("student@example.com")).thenReturn(true);
        String token = service.createResetToken("student@example.com");

        service.resetPassword(token, "NewStrong1");

        assertThatThrownBy(() -> service.resetPassword(token, "AnotherStrong1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid reset token");
    }

    @Test
    void resetPassword_removesExpiredTokenAfterFailure() {
        PasswordResetService service = new PasswordResetService(userRepository, authService, -1);
        when(userRepository.existsByEmail("student@example.com")).thenReturn(true);
        String token = service.createResetToken("student@example.com");

        assertThatThrownBy(() -> service.resetPassword(token, "NewStrong1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reset token has expired");

        assertThatThrownBy(() -> service.resetPassword(token, "NewStrong1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid reset token");
    }

    @Test
    void resetPassword_usesNormalizedEmailFromTokenCreation() {
        PasswordResetService service = new PasswordResetService(userRepository, authService, 15);
        when(userRepository.existsByEmail("student@example.com")).thenReturn(true);
        String token = service.createResetToken("  STUDENT@EXAMPLE.COM ");

        service.resetPassword(token, "NewStrong1");

        verify(authService).resetPassword("student@example.com", "NewStrong1");
    }
}

