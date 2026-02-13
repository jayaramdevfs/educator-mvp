package com.educator.common.security;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserIdentityUtilTest {

    @Test
    void toStableUuid_returnsDeterministicValueForSameEmail() {
        UUID first = UserIdentityUtil.toStableUuid("student@example.com");
        UUID second = UserIdentityUtil.toStableUuid("student@example.com");

        assertThat(first).isEqualTo(second);
    }

    @Test
    void toStableUuid_isCaseInsensitive() {
        UUID lower = UserIdentityUtil.toStableUuid("student@example.com");
        UUID upper = UserIdentityUtil.toStableUuid("STUDENT@EXAMPLE.COM");

        assertThat(lower).isEqualTo(upper);
    }

    @Test
    void toStableUuid_returnsDifferentValuesForDifferentEmails() {
        UUID first = UserIdentityUtil.toStableUuid("student@example.com");
        UUID second = UserIdentityUtil.toStableUuid("instructor@example.com");

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void toStableUuid_matchesJdkNameUuidComputation() {
        UUID expected = UUID.nameUUIDFromBytes(
                "student@example.com".toLowerCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8)
        );

        UUID actual = UserIdentityUtil.toStableUuid("student@example.com");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toStableUuid_returnsNonNullUuid() {
        UUID actual = UserIdentityUtil.toStableUuid("student@example.com");

        assertThat(actual).isNotNull();
    }

    @Test
    void toStableUuid_throwsWhenEmailIsNull() {
        assertThatThrownBy(() -> UserIdentityUtil.toStableUuid(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Authenticated user email is required");
    }

    @Test
    void toStableUuid_throwsWhenEmailIsEmpty() {
        assertThatThrownBy(() -> UserIdentityUtil.toStableUuid(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Authenticated user email is required");
    }

    @Test
    void toStableUuid_throwsWhenEmailIsBlank() {
        assertThatThrownBy(() -> UserIdentityUtil.toStableUuid("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Authenticated user email is required");
    }
}

