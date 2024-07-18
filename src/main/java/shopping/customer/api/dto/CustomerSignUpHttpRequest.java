package shopping.customer.api.dto;

import shopping.customer.application.command.CustomerSignUpCommand;

public class CustomerSignUpHttpRequest {
    private String email;
    private String name;
    private String password;
    private String brith;
    private String address;
    private String phone;

    public CustomerSignUpHttpRequest() {
    }

    public CustomerSignUpHttpRequest(final String email, final String name, final String password, final String brith, final String address, final String phone) {
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

    public CustomerSignUpCommand toCommand() {
        return new CustomerSignUpCommand(
                this.email,
                this.name,
                this.password,
                this.brith,
                this.address,
                this.phone
        );
    }
}