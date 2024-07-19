package shopping.fake;

import java.util.List;
import shopping.product.domain.ProfanityChecker;

public class FakeProfanityChecker implements ProfanityChecker {

    private static final List<String> profanities = List.of("비속어", "욕", "메롱");

    @Override
    public boolean isProfanity(final String word) {
        return profanities.stream()
                .anyMatch(word::contains);
    }
}
