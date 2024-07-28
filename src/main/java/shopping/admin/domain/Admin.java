package shopping.admin.domain;

public record Admin(long id, String name, String email, String password) {
    public boolean isSamePassword(final String password) {
        return this.password.equals(password);
    }
}