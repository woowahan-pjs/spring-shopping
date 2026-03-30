package shopping.member.domain;

import java.util.UUID;

public interface TokenProvider {

    String createToken(UUID memberId);

    UUID extractMemberId(String token);
}
