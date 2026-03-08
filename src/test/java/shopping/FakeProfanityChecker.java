package shopping;

import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import shopping.product.ProfanityChecker;

@Component
@Primary
public class FakeProfanityChecker implements ProfanityChecker {

    private static final Set<String> BAD_WORDS = Set.of("badword", "profanity");

    @Override
    public boolean containsProfanity(String text) {
        String lower = text.toLowerCase();
        return BAD_WORDS.stream().anyMatch(lower::contains);
    }
}
