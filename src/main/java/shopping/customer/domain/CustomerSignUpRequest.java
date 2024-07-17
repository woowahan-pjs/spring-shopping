package shopping.customer.domain;

public record CustomerSignUpRequest(String email, String name, String password, String birth, String address, String phone) {
}