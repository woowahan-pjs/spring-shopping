package shopping.auth.service.token;

public interface TokenProvider {
    String createToken(Long memberId);
    Long getMemberId(String token);
}