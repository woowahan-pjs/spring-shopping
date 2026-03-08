package shopping.wish.infrastructure.persistence.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shopping.wish.domain.WishEntity;
import shopping.wish.domain.WishRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WishRepositoryImpl implements WishRepository {

    private final WishJpaRepository wishJpaRepository;

    @Override
    public WishEntity save(WishEntity wish) {
        return wishJpaRepository.save(wish);
    }

    @Override
    public Optional<WishEntity> findByIdAndMemberId(Long wishId, Long memberId) {
        return wishJpaRepository.findByIdAndMemberId(wishId, memberId);
    }

    @Override
    public List<WishEntity> findAllByMemberId(Long memberId) {
        return wishJpaRepository.findAllByMemberId(memberId);
    }

    @Override
    public boolean existsByMemberIdAndProductIdWithLock(Long memberId, Long productId) {
        return wishJpaRepository.findByMemberIdAndProductId(memberId, productId).isPresent();
    }

    @Override
    public void delete(WishEntity wish) {
        wishJpaRepository.delete(wish);
    }
}
