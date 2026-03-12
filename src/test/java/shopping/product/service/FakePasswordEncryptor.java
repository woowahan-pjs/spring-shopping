package shopping.product.service;

import shopping.member.service.PasswordEncryptor;

public class FakePasswordEncryptor implements PasswordEncryptor {
    @Override
    public String encrypt(String rawPassword) {
        return rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}
