package shopping.product.service;

import shopping.common.client.ProfanityClient;

public class FakeProfanityClient implements ProfanityClient {
    private boolean profane = false;

    public void setProfane(boolean profane) {
        this.profane = profane;
    }

    @Override
    public boolean containsProfanity(String text) {
        return profane;
    }
}
