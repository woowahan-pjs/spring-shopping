package shopping.infra.client.purgomalum;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
class PurgoMalumClientTest {

    private final PurgoMalumClient client = PurgoMalumClientHelper.client();

    @Test
    @DisplayName("외부 요청을 성공적으로 처리합니다.")
    void success() {
        // given
        final ContainsProfanityRequest request = new ContainsProfanityRequest("ass");

        // when
        final String response = client.get("/service/containsprofanity", request);

        // then
        assertThat(response).isEqualTo("true");
    }
}
