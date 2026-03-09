package shopping.wish.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Product;
import shopping.product.service.ProductQueryService;
import shopping.wish.domain.Wish;
import shopping.wish.repository.WishRepository;
import shopping.wish.service.dto.WishAddInput;
import shopping.wish.service.dto.WishAddOutput;

@Service
@RequiredArgsConstructor
@Transactional
public class WishCommandService {
    private final WishRepository wishRepository;
    private final ProductQueryService productQueryService;

    public WishAddOutput add(WishAddInput input) {
        Product product = productQueryService.getProduct(input.productId());
        Wish wish = wishRepository.save(createWish(product, input.memberId()));
        return WishAddOutput.of(wish, product);
    }

    public void delete(Long wishId, Long memberId) {
        Wish wish = wishRepository.findByIdAndMemberIdAndDeletedFalse(wishId, memberId)
                                  .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 위시리스트입니다."));
        wish.delete();
    }

    private Wish createWish(Product product, Long memberId) {
        return Wish.builder()
                   .memberId(memberId)
                   .productId(product.getId())
                   .build();
    }
}