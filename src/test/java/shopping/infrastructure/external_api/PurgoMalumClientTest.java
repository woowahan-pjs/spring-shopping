package shopping.infrastructure.external_api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(PurgoMalumClient.class)
class PurgoMalumClientTest {
    @Autowired
    private PurgoMalumClient purgoMalumClient;

    @Autowired
    private MockRestServiceServer serviceServer;

    @Test
    @DisplayName("비속어 없이 유효한 text면 true 반환")
    void should_return_true_when_api_responds_success() {
        serviceServer.expect(requestTo("https://www.purgomalum.com/service/containsprofanity?text=badword"))
                .andRespond(withSuccess("true", MediaType.TEXT_PLAIN));

        boolean isProfane = purgoMalumClient.containsProfanity("badword");

        assertThat(isProfane).isTrue();
    }
}