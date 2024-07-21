package shopping.wishproduct.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import shopping.member.domain.Member;
import shopping.product.domain.Product;
import shopping.wishproduct.domain.WishProduct;

public class TestWishProductRepository implements WishProductRepository {
    private final Map<Long, WishProduct> database = new HashMap<>();
    private long autoIncrement;

    @Override
    public boolean existsByMemberAndProduct(Member member, Product product) {
        return database.values()
                .stream()
                .anyMatch(wishProduct -> wishProduct.getMember().equals(member) && wishProduct.getProduct().equals(product));
    }

    @Override
    public void save(WishProduct wishProduct) {
        database.put(++autoIncrement, wishProduct);
    }

    @Override
    public List<WishProduct> findAllByMember(Member member) {
        return database.values()
                .stream()
                .filter(wishProduct -> wishProduct.getMember().equals(member))
                .toList();
    }

    @Override
    public void remove(Member member, Long productId) {
        database.values()
                .stream()
                .filter(wishProduct -> wishProduct.getMember().equals(member) && wishProduct.getProductId().equals(productId))
                .findAny()
                .ifPresent(wishProduct -> database.remove(wishProduct.getId()));
    }
}
