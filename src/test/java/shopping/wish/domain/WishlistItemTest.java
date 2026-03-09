package shopping.wish.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WishlistItemTest {
    @Test
    @DisplayName("위시리스트 아이템을 만들면 전달한 값으로 저장한다")
    void createInitializeFields() {
        Wishlist wishlist = Wishlist.create(1L);

        WishlistItem item = WishlistItem.create(wishlist, 10L, WishQuantity.from(2));

        assertThat(item.getWishlist()).isEqualTo(wishlist);
        assertThat(item.getProductId()).isEqualTo(10L);
        assertThat(item.getQuantity()).isEqualTo(2);
        assertThat(item.getAddedAt()).isNotNull();
    }

    @Test
    @DisplayName("위시리스트 아이템을 만들면 추가 시간을 즉시 채운다")
    void createInitializeAddedAt() {
        WishlistItem item = WishlistItem.create(Wishlist.create(1L), 10L, WishQuantity.from(2));

        assertThat(item.getAddedAt()).isNotNull();
    }
}
