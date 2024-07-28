package shopping.admin.infrastructure.api.dto;

import shopping.admin.application.command.AdminSignUpCommand;

public class AdminSignUpHttpRequest {

    private String email;
    private String name;
    private String password;

    private AdminSignUpHttpRequest() {
    }

    public AdminSignUpHttpRequest(final String email, String name, final String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public AdminSignUpCommand toCommand() {
        return new AdminSignUpCommand(email, name, password);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}