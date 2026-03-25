package shopping.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompositeSlangChecker implements SlangChecker {
    private final EnglishSlangChecker englishSlangChecker;
    private final KoreanSlangChecker koreanSlangChecker;

    @Override
    public boolean containsSlang(String text) {
        boolean containsEnglishSlang = englishSlangChecker.containsSlang(text);
        if (containsEnglishSlang) {
            return true;
        }

        return koreanSlangChecker.containsSlang(text);
    }
}
