package shopping.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorMessage {

    LOGIN_FAILED("이메일 또는 비밀번호가 올바르지 않습니다"),
    ALREADY_EXISTS_EMAIL("이미 사용중인 이메일입니다");

    private final String description;
}
