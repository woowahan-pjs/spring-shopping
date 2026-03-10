package shopping.member.domain;

public interface TokenProvider {

    String createToken(String email);

    String extractEmail(String token);
}
