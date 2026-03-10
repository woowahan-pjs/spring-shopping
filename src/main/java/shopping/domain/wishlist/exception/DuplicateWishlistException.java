package shopping.domain.wishlist.exception;

import shopping.domain.common.exception.DomainException;

public class DuplicateWishlistException extends DomainException {
    public DuplicateWishlistException(Long productId) {
        super(String.format("이미 위시리스트에 추가된 상품입니다. (상품 ID: %d)", productId));
    }
}
