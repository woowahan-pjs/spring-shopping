package shopping.product.domain;

import shopping.infra.exception.ShoppingBusinessException;

public class NotFoundProductException extends ShoppingBusinessException {

    public NotFoundProductException() {
        super("상품이 존재하지 않습니다.");
    }
}
