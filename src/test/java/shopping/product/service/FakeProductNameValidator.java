package shopping.product.service;

import shopping.common.client.ProductNameValidator;

public class FakeProductNameValidator implements ProductNameValidator {

    private boolean profane = false;

    public void setProfane(boolean profane) {
        this.profane = profane;
    }

    @Override
    public boolean containsProfanity(String text) {
        return profane;
    }
}
