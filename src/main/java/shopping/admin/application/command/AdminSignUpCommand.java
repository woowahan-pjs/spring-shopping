package shopping.admin.application.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import shopping.common.CommandValidating;

public record AdminSignUpCommand(
        @Email String email,
        @NotBlank String name,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{12,}$") String password
) implements CommandValidating<AdminSignUpCommand> {

    public AdminSignUpCommand(final String email, final String name, final String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.validateSelf(this);
    }
}
