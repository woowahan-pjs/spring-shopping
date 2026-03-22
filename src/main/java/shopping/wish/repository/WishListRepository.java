package shopping.wish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.wish.domain.WishList;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    Optional<WishList> findByMemberId(Long memberId);
}
