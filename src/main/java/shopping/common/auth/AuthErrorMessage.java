package shopping.common.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorMessage {
    NOT_AUTHORIZED("접근 권한이 없습니다"),
    ;
    private final String description;
}
