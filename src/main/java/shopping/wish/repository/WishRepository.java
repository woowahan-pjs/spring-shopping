package shopping.wish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shopping.wish.domain.Wish;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findByMemberIdAndDeletedFalse(Long memberId, Pageable pageable);
    Optional<Wish> findByIdAndMemberIdAndDeletedFalse(Long id, Long memberId);
}
