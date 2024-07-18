package shopping.seller.api.dto;

import shopping.seller.application.command.SellerSignInCommand;

public class SellerSignInHttpRequest {

    private String email;
    private String password;

    public SellerSignInHttpRequest() {
    }

    public SellerSignInHttpRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public SellerSignInCommand toCommand() {
        return new SellerSignInCommand(email, password);
    }
}