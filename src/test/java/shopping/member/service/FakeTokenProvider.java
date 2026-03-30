package shopping.member.service;

import shopping.member.domain.*;

import java.util.UUID;

public class FakeTokenProvider implements TokenProvider {

    private static final String PREFIX = "token:";

    @Override
    public String createToken(UUID memberId) {
        return PREFIX + memberId;
    }

    @Override
    public UUID extractMemberId(String token) {
        return UUID.fromString(token.substring(PREFIX.length()));
    }
}
