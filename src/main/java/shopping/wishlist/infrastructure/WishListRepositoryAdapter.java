package shopping.wishlist.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.wishlist.domain.WishList;
import shopping.wishlist.domain.WishListRepository;
import shopping.wishlist.infrastructure.persistence.WishListEntity;
import shopping.wishlist.infrastructure.persistence.WishListEntityJpaRepository;

import static shopping.wishlist.infrastructure.WishListEntityMapper.domainToEntity;
import static shopping.wishlist.infrastructure.WishListEntityMapper.entityToDomain;

@Component
public class WishListRepositoryAdapter implements WishListRepository {

    private final WishListEntityJpaRepository wishListEntityJpaRepository;

    public WishListRepositoryAdapter(final WishListEntityJpaRepository wishListEntityJpaRepository) {
        this.wishListEntityJpaRepository = wishListEntityJpaRepository;
    }

    @Transactional
    @Override
    public WishList save(final WishList wishList) {
        final WishListEntity wishListEntity = wishListEntityJpaRepository.findByCustomerIdAndProductId(wishList.customerId(), wishList.productId())
                .orElseGet(() -> wishListEntityJpaRepository.save(domainToEntity(wishList)));
        return entityToDomain(wishListEntity);
    }
}
