package shopping;

import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import shopping.product.domain.ProfanityChecker;

@Component
@Primary
public class FakeProfanityChecker implements ProfanityChecker {

    private static final Set<String> BAD_WORDS = Set.of("badword", "profanity");

    private boolean shouldFail;

    @Override
    public boolean containsProfanity(String text) {
        if (shouldFail) {
            throw new RuntimeException("External profanity API failure");
        }
        String lower = text.toLowerCase();
        return BAD_WORDS.stream().anyMatch(lower::contains);
    }

    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }
}
