package shopping.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("[상품] 복합 비속어 검사기 단위 테스트")
class CompositeSlangCheckerTest {
    @Mock
    private EnglishSlangChecker englishSlangChecker;

    @Mock
    private KoreanSlangChecker koreanSlangChecker;

    @Test
    @DisplayName("영문 검사기가 비속어를 찾으면 바로 true를 돌려준다")
    void containsSlangReturnTrueWhenEnglishCheckerMatches() {
        // given
        CompositeSlangChecker checker = new CompositeSlangChecker(englishSlangChecker, koreanSlangChecker);
        when(englishSlangChecker.containsSlang("fuck")).thenReturn(true);

        // when
        boolean result = checker.containsSlang("fuck");

        // then
        assertThat(result).isTrue();
        verify(koreanSlangChecker, never()).containsSlang("fuck");
    }

    @Test
    @DisplayName("영문 검사기가 놓치면 한국어 검사기로 이어서 확인한다")
    void containsSlangCheckKoreanWhenEnglishCheckerMisses() {
        // given
        CompositeSlangChecker checker = new CompositeSlangChecker(englishSlangChecker, koreanSlangChecker);
        when(englishSlangChecker.containsSlang("시 발")).thenReturn(false);
        when(koreanSlangChecker.containsSlang("시 발")).thenReturn(true);

        // when
        boolean result = checker.containsSlang("시 발");

        // then
        assertThat(result).isTrue();
    }
}
