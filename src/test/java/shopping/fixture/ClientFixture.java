package shopping.fixture;

import shopping.fake.FakePasswordEncoder;
import shopping.member.client.domain.Client;
import shopping.member.common.domain.Password;

public class ClientFixture {

    public static Client createClient() {
        final Password password = new Password("1234", new FakePasswordEncoder());
        return new Client("test@test.com", password, "test");
    }
}
