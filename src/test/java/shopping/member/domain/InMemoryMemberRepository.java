package shopping.member.domain;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

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
    public Optional<Member> findByEmail(String email) {
        return memberMap.values().stream()
                .filter(m -> m.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(memberMap.get(memberId));
    }
}
