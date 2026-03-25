package shopping.wish.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Product;
import shopping.product.service.ProductQueryService;
import shopping.wish.domain.Wish;
import shopping.wish.repository.WishRepository;
import shopping.wish.service.dto.WishOutput;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishQueryService {
    private final WishRepository wishRepository;
    private final ProductQueryService productQueryService;

    public Page<WishOutput> findWishWithPage(Long memberId, Pageable pageable) {
        Page<Wish> wishes = wishRepository.findByMemberIdAndDeletedFalse(memberId, pageable);
        Map<Long, Product> productMap = findProductMap(wishes);
        return wishes.map(wish -> toOutput(wish, productMap));
    }

    private Map<Long, Product> findProductMap(Page<Wish> wishes) {
        List<Long> productIds = wishes.stream()
                                      .map(Wish::getProductId)
                                      .toList();
        return productQueryService.findProductMap(productIds);
    }

    private WishOutput toOutput(Wish wish, Map<Long, Product> productMap) {
        Product product = productMap.get(wish.getProductId());
        return product != null ? WishOutput.of(wish, product) : WishOutput.deleted(wish);
    }
}
