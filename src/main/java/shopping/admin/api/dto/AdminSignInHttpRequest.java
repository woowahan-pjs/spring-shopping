package shopping.admin.api.dto;

import shopping.admin.application.command.AdminSignInCommand;

public class AdminSignInHttpRequest {

    private String email;
    private String password;

    public AdminSignInHttpRequest() {
    }

    public AdminSignInHttpRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public AdminSignInCommand toCommand() {
        return new AdminSignInCommand(email, password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}