package shopping.member.client.applicaton.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientCreateRequest(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String memberName
) {

}
