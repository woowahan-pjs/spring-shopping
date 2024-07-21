package shopping.customer.domain;

public record Customer(long id, String email, String name, String password, String birth, String address,
                       String phone) {

    public boolean isSamePassword(final String password) {
        return this.password.equals(password);
    }
}