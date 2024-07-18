package shopping.seller.application.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import shopping.common.CommandValidating;

public record SellerSignInCommand(
        @Email String email,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{12,}$") String password
) implements CommandValidating {
    public SellerSignInCommand(final String email, final String password) {
        this.email = email;
        this.password = password;
        validateSelf(this);
    }
}
