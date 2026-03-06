package shopping.product.service;

import shopping.common.client.ProductNameValidator;

public class FakeProductNameValidator implements ProductNameValidator {

    private boolean profane = false;

    public void setProfane(boolean profane) {
        this.profane = profane;
    }

    @Override
    public void validate(String name) {
        if (profane) {
            throw new IllegalArgumentException("상품명에 비속어가 포함되어 있습니다.");
        }
    }
}
