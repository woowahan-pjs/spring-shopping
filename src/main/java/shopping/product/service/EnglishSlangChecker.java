package shopping.product.service;

import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shopping.product.port.out.EnglishSlangVerificationPort;

@Component
@RequiredArgsConstructor
public class EnglishSlangChecker {
    private static final List<Pattern> LOCAL_SLANG_PATTERNS = List.of(
            Pattern.compile("f+u+c+k+"),
            Pattern.compile("s+h+i+t+"),
            Pattern.compile("b+i+t+c+h+"),
            Pattern.compile("a+s+s+h+o+l+e+"),
            Pattern.compile("c+u+n+t+"),
            Pattern.compile("b+a+s+t+a+r+d+")
    );

    private final EnglishSlangVerificationPort englishSlangVerificationPort;

    public boolean containsSlang(String text) {
        String normalizedText = SlangTextNormalizer.normalizeAscii(text);
        if (hasNoNormalizedAsciiText(normalizedText)) {
            return false;
        }

        if (containsRemoteSlang(text)) {
            return true;
        }

        if (containsLocalSlang(normalizedText)) {
            return true;
        }

        if (isAlreadyNormalized(text, normalizedText)) {
            return false;
        }

        return containsRemoteSlang(normalizedText);
    }

    private boolean containsRemoteSlang(String text) {
        return englishSlangVerificationPort.containsSlang(text);
    }

    private boolean hasNoNormalizedAsciiText(String normalizedText) {
        return normalizedText.isBlank();
    }

    private boolean containsLocalSlang(String text) {
        return LOCAL_SLANG_PATTERNS.stream()
                .anyMatch(pattern -> pattern.matcher(text).find());
    }

    private boolean isAlreadyNormalized(String originalText, String normalizedText) {
        return normalizedText.equals(originalText);
    }
}
