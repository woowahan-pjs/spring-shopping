package shopping.member;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryMemberRepository implements MemberRepository {

    private final Map<Long, Member> members = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Member save(Member member) {
        Long id = idGenerator.getAndIncrement();
        Member saved = new Member(id, member.getEmail(), member.getPassword());
        members.put(id, saved);
        return saved;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return members.values().stream().filter(m -> m.getEmail().equals(email)).findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        return members.values().stream().anyMatch(m -> m.getEmail().equals(email));
    }
}
