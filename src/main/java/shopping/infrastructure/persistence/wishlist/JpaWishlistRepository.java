package shopping.infrastructure.persistence.wishlist;

import org.springframework.stereotype.Repository;
import shopping.domain.wishlist.Wishlist;
import shopping.domain.repository.WishlistRepository;

import java.util.List;

@Repository
public class JpaWishlistRepository implements WishlistRepository {
    private final SpringDataJpaWishlistRepository repository;

    public JpaWishlistRepository(SpringDataJpaWishlistRepository repository) {
        this.repository = repository;
    }

    @Override
    public Wishlist save(Wishlist wishlist) {
        return repository.save(wishlist);
    }

    @Override
    public boolean existsByMemberIdAndProductId(Long memberId, Long productId) {
        return repository.existsByMemberIdAndProductId(memberId, productId);
    }

    @Override
    public List<Wishlist> findAllByMemberIdWithProduct(Long memberId) {
        return repository.findAllByMemberIdWithProduct(memberId);
    }

    @Override
    public void deleteByMemberIdAndProductId(Long memberId, Long productId) {
        repository.deleteByMemberIdAndProductId(memberId, productId);
    }
}
