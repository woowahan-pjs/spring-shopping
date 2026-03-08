package shopping.wish.domain;

import java.util.List;
import java.util.Optional;

public interface WishRepository {

    WishEntity save(WishEntity wish);

    Optional<WishEntity> findByIdAndMemberId(Long wishId, Long memberId);

    List<WishEntity> findAllByMemberId(Long memberId);

    boolean existsByMemberIdAndProductIdWithLock(Long memberId, Long productId);

    void delete(WishEntity wish);
}
