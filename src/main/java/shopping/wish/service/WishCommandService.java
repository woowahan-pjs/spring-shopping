package shopping.wish.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.service.ProductQueryService;
import shopping.product.service.dto.ProductOutput;
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
        ProductOutput product = productQueryService.findProduct(input.productId())
                                                   .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        wishRepository.findByMemberIdAndProductIdAndDeletedFalse(input.memberId(), product.id())
                      .ifPresent(w -> { throw new IllegalArgumentException("이미 위시리스트에 있는 상품입니다."); });
        Wish wish = wishRepository.save(Wish.create(input.memberId(), product.id()));
        return WishAddOutput.of(wish, product);
    }

    public void delete(Long wishId, Long memberId) {
        Wish wish = wishRepository.findByIdAndMemberIdAndDeletedFalse(wishId, memberId)
                                  .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 위시리스트입니다."));
        wish.delete();
    }
}
