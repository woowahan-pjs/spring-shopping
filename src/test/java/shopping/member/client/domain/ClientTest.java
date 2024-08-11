package shopping.member.client.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import shopping.fixture.ClientFixture;

@DisplayName("Client")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ClientTest {

    @Test
    void Client를_생성할_수_있다() {
        assertThatNoException()
                .isThrownBy(ClientFixture::createClient);
    }
}