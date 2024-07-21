package shopping.wishproduct.infra;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shopping.member.domain.Member;
import shopping.product.domain.Product;
import shopping.wishproduct.application.WishProductRepository;
import shopping.wishproduct.domain.WishProduct;

@Repository
public class WishProductRepositoryImpl implements WishProductRepository {
    private final WishProductJpaRepository wishProductJpaRepository;

    public WishProductRepositoryImpl(WishProductJpaRepository wishProductJpaRepository) {
        this.wishProductJpaRepository = wishProductJpaRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByMemberAndProduct(Member member, Product product) {
        return wishProductJpaRepository.existsByMemberAndProduct(member, product);
    }

    @Transactional
    @Override
    public void save(WishProduct wishProduct) {
        wishProductJpaRepository.save(wishProduct);
    }

    @Transactional(readOnly = true)
    @Override
    public List<WishProduct> findAllByMember(Member member) {
        return wishProductJpaRepository.findAllByMemberWithProduct(member);
    }

    @Transactional
    @Override
    public void remove(Member member, Long productId) {
        wishProductJpaRepository.deleteByMemberAndProductId(member, productId);
    }
}
