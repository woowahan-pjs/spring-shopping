package shopping.member;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryMemberRepository implements MemberRepository {

    private final Map<Long, Member> members = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Member save(Member member) {
        if (member.getId() == null) {
            Long id = idGenerator.getAndIncrement();
            Member saved = member.withId(id);
            members.put(id, saved);
            return saved;
        }
        members.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(members.get(id));
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
