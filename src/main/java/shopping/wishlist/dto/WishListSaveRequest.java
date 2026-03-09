package shopping.wishlist.dto;

import java.util.List;

public record WishListSaveRequest(List<WishListItemSaveRequest> items) {

    public List<Long> extractIds() {
        return items.stream().map(WishListItemSaveRequest::productId).toList();
    }
}
