package shopping.domain;

public class WishlistItem {
    private Long memberId;
    private Long productId;

    public WishlistItem(Long memberId, Long productId) {
        validatedMemberId(memberId);
        validateProductId(productId);
        this.memberId = memberId;
        this.productId = productId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    private void validatedMemberId(Long memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("회원 아이디는 필수입니다.");
        }
    }

    private void validateProductId(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("상품 아이디는 필수입니다.");
        }
    }

}
