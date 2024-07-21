package shopping.wishproduct.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import shopping.member.domain.Member;
import shopping.product.domain.Product;
import shopping.wishproduct.domain.WishProduct;

public interface WishProductJpaRepository extends JpaRepository<WishProduct, Long> {
    boolean existsByMemberAndProduct(Member member, Product product);

    @Query("select w from WishProduct w join fetch w.product where w.member = :member")
    List<WishProduct> findAllByMemberWithProduct(Member member);

    @Modifying
    @Query("delete from WishProduct w where w.member = :member and w.product.id = :productId")
    void deleteByMemberAndProductId(Member member, Long productId);
}
