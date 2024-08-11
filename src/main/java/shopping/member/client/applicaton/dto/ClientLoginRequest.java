package shopping.member.client.applicaton.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientLoginRequest(
        @NotBlank
        String email,
        @NotBlank
        String password
) {

}
