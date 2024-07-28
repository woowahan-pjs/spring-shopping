package shopping.member.owner.application.dto;

import jakarta.validation.constraints.NotBlank;

public record OwnerCreateRequest(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String memberName
) {

}
