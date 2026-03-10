package shopping.common.auth;

public interface TokenProvider {

    String generateToken(Long memberId, String role);

    Long extractMemberId(String token);

    String extractRole(String token);
}
