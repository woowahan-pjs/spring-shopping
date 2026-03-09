package shopping.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.product.port.out.EnglishSlangVerificationPort;

@ExtendWith(MockitoExtension.class)
class EnglishSlangCheckerTest {
    @Mock
    private EnglishSlangVerificationPort englishSlangVerificationPort;

    @Test
    @DisplayName("영문 비속어를 바로 감지하면 true를 돌려준다")
    void containsSlangReturnTrueWhenApiDetectsSlang() {
        // given
        EnglishSlangChecker checker = new EnglishSlangChecker(englishSlangVerificationPort);
        when(englishSlangVerificationPort.containsSlang("fuck")).thenReturn(true);

        // when
        boolean result = checker.containsSlang("fuck");

        // then
        assertThat(result).isTrue();
        verify(englishSlangVerificationPort).containsSlang("fuck");
    }

    @Test
    @DisplayName("구분자를 섞은 영문 비속어도 로컬 패턴으로 감지한다")
    void containsSlangDetectObfuscatedEnglishSlang() {
        // given
        EnglishSlangChecker checker = new EnglishSlangChecker(englishSlangVerificationPort);
        when(englishSlangVerificationPort.containsSlang("f.u.c.k")).thenReturn(false);

        // when
        boolean result = checker.containsSlang("f.u.c.k");

        // then
        assertThat(result).isTrue();
        verify(englishSlangVerificationPort).containsSlang("f.u.c.k");
        verify(englishSlangVerificationPort, never()).containsSlang("fuck");
    }

    @Test
    @DisplayName("영문 문자가 없으면 정규화 이후 추가 검사 없이 false를 돌려준다")
    void containsSlangReturnFalseWhenAsciiTextIsBlank() {
        // given
        EnglishSlangChecker checker = new EnglishSlangChecker(englishSlangVerificationPort);

        // when
        boolean result = checker.containsSlang("상품");

        // then
        assertThat(result).isFalse();
        verifyNoInteractions(englishSlangVerificationPort);
    }

    @Test
    @DisplayName("정규화한 텍스트는 PurgoMalum으로 한 번 더 검사한다")
    void containsSlangCheckNormalizedAsciiWithApi() {
        // given
        EnglishSlangChecker checker = new EnglishSlangChecker(englishSlangVerificationPort);
        when(englishSlangVerificationPort.containsSlang("d-a-m-n")).thenReturn(false);
        when(englishSlangVerificationPort.containsSlang("damn")).thenReturn(true);

        // when
        boolean result = checker.containsSlang("d-a-m-n");

        // then
        assertThat(result).isTrue();
        verify(englishSlangVerificationPort).containsSlang("d-a-m-n");
        verify(englishSlangVerificationPort).containsSlang("damn");
    }
}
