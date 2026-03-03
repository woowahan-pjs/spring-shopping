package shopping.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import shopping.auth.domain.ValidEmail;
import shopping.auth.domain.ValidPassword;

@Schema(name = "[회원] 로그인 요청 DTO", description = "로그인 요청을 위한 DTO 입니다.")
public record LoginRequest(
        @ValidEmail
                @Schema(
                        description = "이메일, 로컬 파트 64자, 도메인 파트 255자로 총 320자 입력 가능합니다.",
                        example = "test@test.com")
                String email,
        @ValidPassword @Schema(description = "비밀번호", example = "12345678") String password) {}
