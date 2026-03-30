package shopping.product.service;

import shopping.product.domain.*;

import java.util.Set;

public class FakeProfanityChecker implements ProfanityChecker {

    private final Set<String> profanities;

    public FakeProfanityChecker(String... profanities) {
        this.profanities = Set.of(profanities);
    }

    @Override
    public boolean containsProfanity(String text) {
        return profanities.stream().anyMatch(text::contains);
    }
}
