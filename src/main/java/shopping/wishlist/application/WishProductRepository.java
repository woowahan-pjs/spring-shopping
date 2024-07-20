package shopping.wishlist.application;

import java.util.List;
import shopping.member.domain.Member;
import shopping.wishlist.domain.WishProduct;

public interface WishProductRepository {
    void save(WishProduct wishProduct);

    List<WishProduct> findAllByMember(Member member);
}
