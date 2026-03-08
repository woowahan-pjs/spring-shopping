package shopping.domain;

public class WishlistItem {
    private Long memberId;
    private Long productId;

    public WishlistItem(Long memberId, Long productId) {
        this.memberId = memberId;
        this.productId = productId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }
}
