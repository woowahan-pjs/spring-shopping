package shopping.wishlist.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import shopping.wishlist.domain.Wishlist;
import shopping.wishlist.domain.WishlistRepository;

@Repository
public class WishlistRepositoryImpl implements WishlistRepository {
    private final WishlistJpaRepository repository;

    public WishlistRepositoryImpl(WishlistJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Wishlist save(Wishlist wishlist) {
        return repository.save(WishlistEntity.from(wishlist)).toDomain();
    }

    @Override
    public Page<Wishlist> findAllByMemberId(Long memberId, Pageable pageable) {
        return repository.findAllByMemberId(memberId, pageable).map(WishlistEntity::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByMemberIdAndProductId(long memberId, long productId) {
        return repository.existsByMemberIdAndProductId(memberId, productId);
    }

    @Override
    public boolean existsByMemberIdAndId(Long memberId, Long id) {
        return repository.existsByMemberIdAndId(memberId, id);
    }
}
