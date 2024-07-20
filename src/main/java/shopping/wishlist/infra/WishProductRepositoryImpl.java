package shopping.wishlist.infra;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shopping.wishlist.application.WishProductRepository;
import shopping.wishlist.domain.WishProduct;

@Repository
public class WishProductRepositoryImpl implements WishProductRepository {
    private final WishProductJpaRepository wishProductJpaRepository;

    public WishProductRepositoryImpl(WishProductJpaRepository wishProductJpaRepository) {
        this.wishProductJpaRepository = wishProductJpaRepository;
    }

    @Transactional
    @Override
    public void save(WishProduct wishProduct) {
        wishProductJpaRepository.save(wishProduct);
    }
}
