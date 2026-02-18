package shopping.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request body for member registration and login.
 *
 * @author brian.kim
 * @since 1.0
 */
public record MemberRequest(
    @NotBlank @Email String email,
    @NotBlank String password
) {
}
