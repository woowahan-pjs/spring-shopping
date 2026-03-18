package shopping.wish.service.dto;

import shopping.wish.domain.Wish;

public record WishAddInput(Long memberId, Long productId) {
    public Wish toDomain() {
        return Wish.builder()
                   .memberId(memberId)
                   .productId(productId)
                   .build();
    }
}
