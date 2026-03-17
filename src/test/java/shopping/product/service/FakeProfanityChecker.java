package shopping.product.service;

import shopping.common.client.ProfanityChecker;

public class FakeProfanityChecker implements ProfanityChecker {
    private boolean profane = false;

    public void setProfane(boolean profane) {
        this.profane = profane;
    }

    @Override
    public boolean containsProfanity(String text) {
        return profane;
    }
}
