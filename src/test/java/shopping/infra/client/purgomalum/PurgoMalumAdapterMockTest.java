package shopping.infra.client.purgomalum;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PurgoMalumAdapterMockTest {

    @InjectMocks
    private PurgoMalumAdapter purgoMalumAdapter;

    @Mock
    private PurgoMalumClient purgoMalumClient;

    @Nested
    @DisplayName("특정 단어가 비속어인지 판단할 때,")
    class isProfanity {

        @Test
        @DisplayName("비속어가 존재하면 true를 반환합니다.")
        void isTrue() {
            // given
            final String text = "ass";

            given(purgoMalumClient.get(eq(PurgoMalumEndPoint.CONTAINS_PROFANITY.getEndpoint()),
                eq(new ContainsProfanityRequest(text))))
                .willReturn("true");

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

            given(purgoMalumClient.get(eq(PurgoMalumEndPoint.CONTAINS_PROFANITY.getEndpoint()),
                eq(new ContainsProfanityRequest(text))))
                .willReturn("false");

            // when
            final boolean result = purgoMalumAdapter.isProfanity(text);

            // then
            assertThat(result).isFalse();
        }
    }
}
