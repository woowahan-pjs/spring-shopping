package shopping.member.domain;

import jakarta.validation.constraints.NotBlank;

public record MemberCreate(
        @NotBlank String email,
        @NotBlank String password
) {
}
