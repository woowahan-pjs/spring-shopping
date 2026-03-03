package shopping.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "[회원] 로그인 응답 DTO", description = "로그인 성공시, 토큰 정보를 담은 DTO 입니다.")
public record LoginResponse(@Schema(description = "액세스 토큰") String accessToken) {

    public static LoginResponse of(final String accessToken) {
        return new LoginResponse(accessToken);
    }
}
