package com.educator.common.security;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;

public final class UserIdentityUtil {

    private UserIdentityUtil() {
    }

    public static UUID toStableUuid(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Authenticated user email is required");
        }

        return UUID.nameUUIDFromBytes(
                email.toLowerCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8)
        );
    }
}
