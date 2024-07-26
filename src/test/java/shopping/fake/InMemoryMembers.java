package shopping.fake;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import shopping.member.common.domain.Member;

public class InMemoryMembers {

    private final Map<String, Member> members = new HashMap<>();

    public Member save(final Member member) {
        members.put(member.getEmail(), member);
        return member;
    }

    public Optional<Member> findByEmail(final String email) {
        return Optional.ofNullable(members.get(email));
    }
}
