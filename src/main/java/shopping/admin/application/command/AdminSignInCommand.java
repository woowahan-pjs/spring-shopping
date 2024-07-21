package shopping.admin.application.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import shopping.common.CommandValidating;

public record AdminSignInCommand(
        @Email String email,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{12,}$") String password
) implements CommandValidating<AdminSignInCommand> {

    public AdminSignInCommand(final String email, final String password) {
        this.email = email;
        this.password = password;
        this.validateSelf(this);
    }
}
