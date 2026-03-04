package shopping.infra.client.purgomalum;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
class PurgoMalumAdapterTest {

    private final PurgoMalumAdapter purgoMalumAdapter =
            new PurgoMalumAdapter(PurgoMalumClientHelper.client());

    @Nested
    @DisplayName("특정 단어가 비속어인지 판단할 때,")
    class isProfanity {

        @Test
        @DisplayName("비속어가 존재하면 true를 반환합니다.")
        void isTrue() {
            // given
            final String text = "ass";

            // when
            final boolean result = purgoMalumAdapter.isProfanity(text);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("비속어가 존재하지 않으면 false를 반환합니다.")
        void isFalse() {
            // given
            final String text = "703";

            // when
            final boolean result = purgoMalumAdapter.isProfanity(text);

            // then
            assertThat(result).isFalse();
        }
    }
}
