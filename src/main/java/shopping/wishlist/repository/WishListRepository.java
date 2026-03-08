package shopping.wishlist.repository;

import java.util.Optional;

import jakarta.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import shopping.wishlist.domain.WishList;

public interface WishListRepository extends CrudRepository<WishList, Long> {

    @Query(
            value =
                    """
        SELECT w
        FROM WishList w
        JOIN FETCH w.items i
        WHERE w.userId = :userId
        AND i.isUse = true
    """)
    @QueryHints(
            @QueryHint(
                    name = "org.hibernate.comment",
                    value = "WishListRepository.findByUserIdAndIsUse : 특정 회원의 위시 리스트를 조회합니다."))
    Optional<WishList> findByWishList(@Param("userId") final Long userId);
}
