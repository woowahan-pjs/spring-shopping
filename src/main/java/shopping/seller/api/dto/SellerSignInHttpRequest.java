package shopping.seller.api.dto;

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
}