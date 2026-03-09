package shopping.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import shopping.auth.domain.Role;
import shopping.auth.domain.ValidEmail;
import shopping.auth.domain.ValidPassword;

// TODO: email 객체로 분리
@Schema(name = "[회원] 회원 가입 요청 DTO", description = "회원 가입 요청시 필요한 DTO입니다.")
public record RegisterRequest(

    @ValidEmail
    @Schema(description = "이메일, 로컬 파트 64자, 도메인 파트 255자로 총 320자 입력 가능합니다.", example = "test@test.com")
    String email,

    @ValidPassword
    @Schema(description = "비밀번호", example = "12345678")
    String password,

    @NotNull(message = "권한은 필수 입력 값입니다.")
    @Schema(description = "권한, ADMIN | CUSTOMER", example = "ADMIN")
    Role role
) {

}
