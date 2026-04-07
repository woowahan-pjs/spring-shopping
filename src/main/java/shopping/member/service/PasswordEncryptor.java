package shopping.member.service;

public interface PasswordEncryptor {
    String encrypt(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
