package shopping.member;

public interface TokenProvider {

    String createToken(String email);
}
