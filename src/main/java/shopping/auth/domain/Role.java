package shopping.auth.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.ObjectUtils;

import shopping.infra.exception.ShoppingBusinessException;

public enum Role {
    ADMIN,
    CUSTOMER;

    private static final Map<String, Role> ROLE_MAP =
            Arrays.stream(Role.values())
                    .collect(Collectors.toUnmodifiableMap(Role::name, role -> role));

    public static Role convert(final String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new ShoppingBusinessException("유효하지 않은 권한입니다.");
        }

        final Role role = ROLE_MAP.get(name);

        if (ObjectUtils.isEmpty(role)) {
            throw new ShoppingBusinessException("유효하지 않은 권한입니다.");
        }

        return role;
    }
}
