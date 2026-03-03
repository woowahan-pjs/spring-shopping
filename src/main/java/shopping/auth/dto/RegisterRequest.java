package shopping.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import shopping.auth.domain.Role;

// TODO: email 객체로 분리
@Schema(name = "[회원] 회원 가입 요청 DTO", description = "회원 가입 요청시 필요한 DTO입니다.")
public record RegisterRequest(
        @Email(message = "유효한 이메일 형식이 아닙니다.")
                @Size(min = 5, max = 320, message = "이메일은 최소 {min}자에서 최대 {max}자 까지 입력 가능합니다.")
                @NotBlank(message = "이메일은 필수 입력 값입니다.")
                @Schema(
                        description = "이메일, 로컬 파트 64자, 도메인 파트 255자로 총 320자 입력 가능합니다.",
                        example = "test@test.com")
                String email,
        @Size(min = 8, max = 100, message = "비밀번호는 최소 {min}자에서 최대 {max}자 까지 입력 가능합니다.")
                @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
                @Schema(description = "비밀번호", example = "12345678")
                String password,
        @NotNull(message = "권한은 필수 입력 값입니다.")
                @Schema(description = "권한, ADMIN | CUSTOMER", example = "ADMIN")
                Role role) {}
