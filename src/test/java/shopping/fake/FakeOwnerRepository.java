package shopping.fake;

import shopping.member.owner.domain.Owner;
import shopping.member.owner.domain.OwnerRepository;

public class FakeOwnerRepository implements OwnerRepository {

    private final InMemoryMembers inMemoryMembers;

    public FakeOwnerRepository(InMemoryMembers inMemoryMembers) {
        this.inMemoryMembers = inMemoryMembers;
    }

    @Override
    public Owner save(final Owner owner) {
        return (Owner) inMemoryMembers.save(owner);
    }
}
