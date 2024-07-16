package shopping.customer.application.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import shopping.common.CommandValidating;

public record CustomerCommand(
        @Email String email,
        @NotBlank String name,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{12,}$") String password,
        String birth,
        String address,
        String phone
) implements CommandValidating {
    public CustomerCommand(final String email, final String name, final String password, final String birth, final String address, final String phone) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.birth = birth;
        this.address = address;
        this.phone = phone;
        validateSelf(this);
    }
}