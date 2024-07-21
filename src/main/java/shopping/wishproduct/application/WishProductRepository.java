package shopping.wishproduct.application;

import java.util.List;
import shopping.member.domain.Member;
import shopping.product.domain.Product;
import shopping.wishproduct.domain.WishProduct;

public interface WishProductRepository {
    boolean existsByMemberAndProduct(Member member, Product product);

    void save(WishProduct wishProduct);

    List<WishProduct> findAllByMember(Member member);

    void remove(Member member, Long productId);
}
