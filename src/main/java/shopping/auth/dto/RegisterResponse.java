package shopping.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "[회원] 회원 가입 응답 DTO", description = "회원 가입 성공시, 토큰 정보를 담은 DTO 입니다.")
public record RegisterResponse(

    @Schema(description = "액세스 토큰")
    String accessToken
) {

    public static RegisterResponse of(final String accessToken) {
        return new RegisterResponse(accessToken);
    }
}
