package shopping.fake;

import java.util.Optional;
import shopping.member.common.domain.Member;
import shopping.member.common.domain.MemberRepository;

public class FakeMemberRepository implements MemberRepository {

    private final InMemoryMembers inMemoryMembers;

    public FakeMemberRepository(InMemoryMembers inMemoryMembers) {
        this.inMemoryMembers = inMemoryMembers;
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        return inMemoryMembers.findByEmail(email);
    }
}
