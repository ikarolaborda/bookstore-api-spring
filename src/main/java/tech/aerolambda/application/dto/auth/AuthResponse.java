package tech.aerolambda.application.dto.auth;

import tech.aerolambda.application.dto.user.UserResponse;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Long expiresIn,
        UserResponse user
) {
    public AuthResponse(String accessToken, Long expiresIn, UserResponse user) {
        this(accessToken, "Bearer", expiresIn, user);
    }
}
