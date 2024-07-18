package shopping.customer.api.dto;

import shopping.customer.application.command.CustomerSignInCommand;

public class CustomerSignInHttpRequest {
    private String email;
    private String password;

    public CustomerSignInHttpRequest() {
    }

    public CustomerSignInHttpRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public CustomerSignInCommand toCommand() {
        return new CustomerSignInCommand(email, password);
    }
}
