package shopping.auth;

public interface TokenProvider {
    String createToken(Long memberId);
    Long getMemberId(String token);
}
