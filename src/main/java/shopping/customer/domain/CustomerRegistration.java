package shopping.customer.domain;

public record CustomerRegistration(String email, String name, String password, String birth, String address, String phone) {
}