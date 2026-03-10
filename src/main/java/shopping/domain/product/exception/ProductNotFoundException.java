package shopping.domain.product.exception;

import shopping.domain.common.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    private static final String MESSAGE_FORMAT = "존재하지 않는 상품입니다. (ID: %d)";

    public ProductNotFoundException() {
        super("상품을 찾을 수 없습니다.");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(Long productId) {
        super(String.format(MESSAGE_FORMAT, productId));
    }
}
