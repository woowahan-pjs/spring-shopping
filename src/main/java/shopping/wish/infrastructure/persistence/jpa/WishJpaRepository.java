package shopping.wish.infrastructure.persistence.jpa;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import shopping.wish.domain.WishEntity;

import java.util.List;
import java.util.Optional;

public interface WishJpaRepository extends JpaRepository<WishEntity, Long> {

    Optional<WishEntity> findByIdAndMemberId(Long id, Long memberId);

    List<WishEntity> findAllByMemberId(Long memberId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<WishEntity> findByMemberIdAndProductId(Long memberId, Long productId);
}
