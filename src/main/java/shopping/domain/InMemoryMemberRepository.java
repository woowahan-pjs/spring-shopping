package shopping.domain;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryMemberRepository implements MemberRepository {
    private final HashMap<Long, Member> memberMap = new HashMap<>();
    private final AtomicLong idSequence = new AtomicLong();

    @Override
    public Member save(Member member) {
        long id = idSequence.getAndIncrement();
        member.assignId(id);
        memberMap.put(id, member);
        return member;
    }

    @Override
    public Member findByEmail(String email) {
        return memberMap.values().stream()
                .filter(m -> m.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}
