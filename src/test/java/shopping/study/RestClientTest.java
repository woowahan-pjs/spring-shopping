package shopping.study;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.web.client.RestClient;

@Disabled
public class RestClientTest {

    private static final String URL = "https://www.purgomalum.com/service/containsprofanity?text={text}";
    private static final RestClient restClient = RestClient.create();

    @ParameterizedTest
    @CsvSource(value = {
            "fuck:true",
            "apple:false",
            "bitch:true",
            "banana:false",
            "fucking:true"
    }, delimiter = ':')
    void RestClient객체를_만들어_Purogomalum에_비속어체크를_요청한다(String text, boolean res) {
        final boolean response = isProfanity(text);
        assertThat(response).isEqualTo(res);
    }

    private boolean isProfanity(final String text) {
        final String response = restClient.get()
                .uri(URL, text)
                .retrieve()
                .body(String.class);
        return Boolean.parseBoolean(response);
    }
}
