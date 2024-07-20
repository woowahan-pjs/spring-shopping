package shopping.member.client.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import shopping.fake.FakePasswordEncoder;
import shopping.member.common.domain.PasswordEncoder;

@DisplayName("Client")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ClientTest {

    private PasswordEncoder passwordEncoder = new FakePasswordEncoder();

    @Test
    void Client를_생성할_수_있다() {
        assertThatNoException()
                .isThrownBy(() -> createClient());
    }

    private Client createClient() {
        return new Client("test@test.com", "1234", passwordEncoder, "test");
    }
}