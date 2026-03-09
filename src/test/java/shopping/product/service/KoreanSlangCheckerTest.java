package shopping.product.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.core.io.ClassPathResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[상품] 한국어 비속어 검사기 단위 테스트")
class KoreanSlangCheckerTest {
    private final KoreanSlangChecker checker = new KoreanSlangChecker(
            new ClassPathResource("slang/korean-rules.csv")
    );

    @Test
    @DisplayName("띄어쓴 한국어 비속어를 감지한다")
    void containsSlangDetectSpacedKoreanSlang() {
        boolean result = checker.containsSlang("시 발 쿠션");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("초성으로 적은 한국어 비속어를 감지한다")
    void containsSlangDetectJamoSlang() {
        boolean result = checker.containsSlang("ㅅㅂ 에디션");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("시발점은 안전한 표현으로 처리한다")
    void containsSlangIgnoreSafePhrase() {
        boolean result = checker.containsSlang("수학의 시발점");

        assertThat(result).isFalse();
    }
}
