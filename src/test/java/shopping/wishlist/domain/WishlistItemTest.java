package shopping.wishlist.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class WishlistItemTest {

    @Test
    @DisplayName("위시리스트에 상품을 추가한다.")
    void addWishlistItem() {
        Long memberId = 1L;
        Long productId = 1L;
        WishlistItem wishlistItem = new WishlistItem(memberId, productId);

        assertThat(wishlistItem.getMemberId()).isEqualTo(memberId);
        assertThat(wishlistItem.getProductId()).isEqualTo(productId);
    }

    @Test
    @DisplayName("회원 아이디가 없으면 예외가 발생한다.")
    void invalidMemberId() {
        assertThatThrownBy(() -> new WishlistItem(null, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 아이디가 없으면 예외가 발생한다.")
    void invalidProductId() {
        assertThatThrownBy(() -> new WishlistItem(1L, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
