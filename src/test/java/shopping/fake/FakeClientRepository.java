package shopping.fake;

import shopping.member.client.domain.Client;
import shopping.member.client.domain.ClientRepository;

public class FakeClientRepository implements ClientRepository {

    private final InMemoryMembers inMemoryMembers;

    public FakeClientRepository(InMemoryMembers inMemoryMembers) {
        this.inMemoryMembers = inMemoryMembers;
    }

    @Override
    public Client save(final Client client) {
        return (Client) inMemoryMembers.save(client);
    }
}
