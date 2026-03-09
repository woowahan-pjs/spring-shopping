package shopping.wish.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.service.ProductQueryService;
import shopping.wish.domain.Wish;
import shopping.wish.repository.WishRepository;
import shopping.wish.service.dto.WishOutput;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishQueryService {
    private final WishRepository wishRepository;
    private final ProductQueryService productQueryService;

    public Page<WishOutput> findAll(Long memberId, Pageable pageable) {
        return wishRepository.findByMemberIdAndDeletedFalse(memberId, pageable)
                             .map(this::toOutput);
    }

    private WishOutput toOutput(Wish wish) {
        return productQueryService.findProduct(wish.getProductId())
                                  .map(product -> WishOutput.of(wish, product))
                                  .orElseGet(() -> WishOutput.deleted(wish));
    }
}
