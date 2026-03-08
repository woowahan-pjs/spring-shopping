package shopping.domain;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMemberRepository implements MemberRepository {
    private final HashMap<Long, Member> memberMap = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();

    @Override
    public Member save(Member member) {
        long id = counter.getAndIncrement();
        member.assignId(id);
        memberMap.put(id, member);
        return member;
    }

    @Override
    public Member findById(Long id) {
        return memberMap.get(id);
    }
}
