package shopping.member.owner.application.dto;

import jakarta.validation.constraints.NotBlank;

public record OwnerLoginRequest(
        @NotBlank
        String email,
        @NotBlank
        String password
) {

}
