package shopping.seller.api.dto;

import shopping.seller.application.command.SellerSignUpCommand;

public class SellerSignUpHttpRequest {

    private String email;
    private String name;
    private String password;
    private String brith;
    private String address;
    private String phone;

    public SellerSignUpHttpRequest() {
    }

    public SellerSignUpHttpRequest(final String email, final String name, final String password, final String brith, final String address, final String phone) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.brith = brith;
        this.address = address;
        this.phone = phone;
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

    public String getBrith() {
        return brith;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public SellerSignUpCommand toCommand() {
        return new SellerSignUpCommand(email, name, password, brith, address, phone);
    }
}