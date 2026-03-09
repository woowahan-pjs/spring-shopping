package shopping.wish.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.service.ProductQueryService;
import shopping.wish.domain.Wish;
import shopping.wish.repository.WishRepository;
import shopping.wish.service.dto.WishOutput;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishQueryService {
    private final WishRepository wishRepository;
    private final ProductQueryService productQueryService;

    public List<WishOutput> findAll(Long memberId) {
        return wishRepository.findByMemberIdAndDeletedFalse(memberId)
                             .stream()
                             .map(this::toOutput)
                             .toList();
    }

    private WishOutput toOutput(Wish wish) {
        return productQueryService.findProduct(wish.getProductId())
                                  .map(product -> WishOutput.of(wish, product))
                                  .orElseGet(() -> WishOutput.deleted(wish));
    }
}
