package shopping.domain;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMemberRepository implements MemberRepository {
    private final HashMap<Long, Member> members = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();

    @Override
    public Member save(Member member) {
        long id = counter.getAndIncrement();
        member.assignId(id);
        members.put(id, member);
        return member;
    }

}
