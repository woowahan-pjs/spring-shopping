package shopping.member.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import shopping.member.domain.Email;
import shopping.member.domain.Member;

public class TestMemberRepository implements MemberRepository {
    private final Map<Long, Member> database = new HashMap<>();

    @Override
    public boolean existsByEmail(Email email) {
        return database.values()
                .stream()
                .anyMatch(member -> member.hasEqualEmail(email));
    }

    @Override
    public void save(Member member) {
        database.put(member.getId(), member);
    }

    @Override
    public Optional<Member> findByEmail(Email email) {
        return database.values()
                .stream()
                .filter(member -> member.hasEqualEmail(email))
                .findAny();
    }
}
