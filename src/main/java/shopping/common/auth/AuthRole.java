package shopping.common.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthRole {
    USER(1),
    ADMIN(2),
    SUPERADMIN(3);

    private final int level;

    public boolean isAccessible(AuthRole userRole) {
        return userRole.level >= this.level;
    }

    public static AuthRole from(String role) {
        return AuthRole.valueOf(role);
    }
}
