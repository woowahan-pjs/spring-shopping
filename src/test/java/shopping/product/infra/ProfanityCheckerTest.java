package shopping.product.infra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

class ProfanityCheckerTest {

    private ProfanityChecker profanityChecker;

    @BeforeEach
    void setUp() {
        profanityChecker = new ProfanityChecker(RestClient.builder());
    }

    @Test
    @DisplayName("특정 단어가 비속어가 아닌 경우 false 를 반환한다")
    void containsProfanityTrueTest() {
        final String word = "hello";

        assertThat(profanityChecker.containsProfanity(word)).isFalse();
    }

    @Test
    @DisplayName("특정 단어가 비속어인 경우 true 를 반환한다")
    void containsProfanityFalseTest() {
        final String word = "병신";

        assertThat(profanityChecker.containsProfanity(word)).isTrue();
    }
}
