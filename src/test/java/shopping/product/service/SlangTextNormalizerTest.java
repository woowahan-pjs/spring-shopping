package shopping.product.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[상품] 비속어 텍스트 정규화기 단위 테스트")
class SlangTextNormalizerTest {
    @Test
    @DisplayName("한국어 정규화는 구분자를 없애고 반복 문자를 줄인다")
    void normalizeHangulRemoveSeparatorsAndCollapseRepeats() {
        String normalized = SlangTextNormalizer.normalizeHangul("시---이이발!!!");

        assertThat(normalized).isEqualTo("시이발");
    }

    @Test
    @DisplayName("ASCII 정규화는 기호를 없애고 소문자로 바꾼다")
    void normalizeAsciiRemoveSymbolsAndLowercase() {
        String normalized = SlangTextNormalizer.normalizeAscii("F.u.C.k!!");

        assertThat(normalized).isEqualTo("fuck");
    }
}
