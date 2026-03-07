package shopping.member.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
        @Email @NotBlank String email,
        @NotBlank String password
) {
}
