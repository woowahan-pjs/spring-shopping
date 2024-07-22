package shopping.wishlist.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.wishlist.domain.WishList;
import shopping.wishlist.domain.WishListRepository;
import shopping.wishlist.infrastructure.persistence.WishListEntity;
import shopping.wishlist.infrastructure.persistence.WishListEntityJpaRepository;

@Component
public class WishListRepositoryAdapter implements WishListRepository {

    private final WishListEntityJpaRepository wishListEntityJpaRepository;

    public WishListRepositoryAdapter(final WishListEntityJpaRepository wishListEntityJpaRepository) {
        this.wishListEntityJpaRepository = wishListEntityJpaRepository;
    }

    @Transactional
    @Override
    public WishList save(final long productId, final long customerId) {
        final WishListEntity wishListEntity = wishListEntityJpaRepository.findByCustomerIdAndProductId(customerId, productId)
                .orElseGet(() -> wishListEntityJpaRepository.save(new WishListEntity(productId, customerId)));
        return entityToDomain(wishListEntity);
    }

    private WishList entityToDomain(final WishListEntity wishListEntity) {
        return new WishList(wishListEntity.getId(), wishListEntity.getProductId(), wishListEntity.getCustomerId());
    }
}
