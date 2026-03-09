package shopping.wishlist.repository;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import shopping.wishlist.domain.WishList;

import java.util.Optional;

public interface WishListRepository extends CrudRepository<WishList, Long> {

    @Query(value = """
        SELECT w
        FROM WishList w
        LEFT JOIN FETCH w.items i
        WHERE w.userId = :userId
    """)
    @QueryHints( @QueryHint(name = "org.hibernate.comment", value = "WishListRepository.findByWishList : 특정 회원의 전체 위시 리스트를 조회합니다."))
    Optional<WishList> findByWishList(@Param("userId") final Long userId);

    @Query(value = """
        SELECT w
        FROM WishList w
        JOIN FETCH w.items i
        WHERE w.userId = :userId
        AND i.isUse = true
    """)
    @QueryHints(@QueryHint(name = "org.hibernate.comment",value = "WishListRepository.findByWishListAndIsUse : 특정 회원의 활성화 되어 있는 위시 리스트를 조회합니다."))
    Optional<WishList> findByWishListAndIsUse(@Param("userId") final Long userId);
}
