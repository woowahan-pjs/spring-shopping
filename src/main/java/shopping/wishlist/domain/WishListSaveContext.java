package shopping.wishlist.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import shopping.product.domain.Product;
import shopping.wishlist.dto.WishListItemSaveSummary;
import shopping.wishlist.dto.WishListSaveSummary;

/**
 * WishListSaveContext는 위시 리스트 저장 시 필요한 컨텍스트 데이터를 관리하는 클래스입니다.
 * 위시 리스트와 관련된 유효한 상품 정보, 요청 정보, 저장 요약 정보를 통합적으로 제공합니다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WishListSaveContext {

    private Long wishListId;

    private Set<Long> validWishListItemProductsSet;

    private List<Long> requestProductIds;
    private Map<Long, Product> validProductMap;

    private WishListSaveSummary summary;

    public static WishListSaveContext create(final WishList wishList, final List<Long> requestProductIds, final List<Product> validProducts) {
        WishListSaveContext context = new WishListSaveContext();

        context.wishListId = wishList.getId();

        context.validWishListItemProductsSet = wishList.getItems().stream()
            .map(WishListItem::getId)
            .collect(Collectors.toSet());

        context.requestProductIds = requestProductIds;
        context.validProductMap = validProducts.stream()
            .collect(
                Collectors.toMap(Product::getId, Function.identity(), (a, b) -> a));

        context.summary = WishListSaveSummary.create(requestProductIds.size());

        return context;
    }

    public boolean isContainsItem(final Long productId) {
        return validWishListItemProductsSet.contains(productId);
    }

    public boolean isNotContainsProductId(final Long productId) {
        return !validProductMap.containsKey(productId);
    }

    public void addSummary(final WishListItemSaveSummary wishListItemSaveSummary) {
        summary.add(wishListItemSaveSummary);
    }

    public WishListSaveSummary summary() {
        return summary;
    }

    public Long getWishListId() {
        return this.wishListId;
    }

    public Product getValidProduct(final Long productId) {
        return validProductMap.get(productId);
    }

    public List<Long> getRequestProductIds() {
        return requestProductIds;
    }
}
