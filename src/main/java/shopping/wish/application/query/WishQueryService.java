package shopping.wish.application.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.ProductEntity;
import shopping.product.domain.ProductRepository;
import shopping.wish.application.dto.WishGetResponse;
import shopping.wish.domain.WishEntity;
import shopping.wish.domain.WishRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishQueryService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public List<WishGetResponse> getAll(Long memberId) {
        List<WishEntity> wishes = wishRepository.findAllByMemberId(memberId);
        Map<Long, ProductEntity> productMap = getProductMap(wishes);
        return wishes.stream()
            .map(wish -> WishGetResponse.of(wish, productMap.get(wish.getProductId())))
            .toList();
    }

    private Map<Long, ProductEntity> getProductMap(List<WishEntity> wishes) {
        List<Long> productIds = wishes.stream()
            .map(WishEntity::getProductId)
            .toList();
        return productRepository.findAllByIdsNotDeleted(productIds).stream()
            .collect(Collectors.toMap(ProductEntity::getId, p -> p));
    }
}
