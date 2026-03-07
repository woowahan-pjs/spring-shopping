package shopping.wish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.wish.domain.Wish;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByMemberIdAndDeletedFalse(Long memberId);
    Optional<Wish> findByIdAndMemberIdAndDeletedFalse(Long id, Long memberId);
}