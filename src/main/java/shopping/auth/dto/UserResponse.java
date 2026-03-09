package shopping.auth.dto;

import shopping.auth.domain.Role;

public record UserResponse(
    Long userId,
    String email,
    Role role
) {

    public static UserResponse from(final Long userId, final String email, final Role role) {
        return new UserResponse(userId, email, role);
    }
}
