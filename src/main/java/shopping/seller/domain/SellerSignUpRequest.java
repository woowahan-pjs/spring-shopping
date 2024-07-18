package shopping.seller.domain;

public record SellerSignUpRequest(String email, String name, String password, String birth, String address, String phone) {
}