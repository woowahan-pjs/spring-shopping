package shopping.wish.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WishlistTest {
    @Test
    @DisplayName("위시리스트를 만들면 활성 상태로 시작한다")
    void createInitializeActiveWishlist() {
        Wishlist wishlist = Wishlist.create(1L);

        assertThat(wishlist.getMemberId()).isEqualTo(1L);
        assertThat(wishlist.getStatus()).isEqualTo(WishlistStatus.ACTIVE);
        assertThat(wishlist.isActive()).isTrue();
    }

    @Test
    @DisplayName("위시리스트를 삭제하면 비활성 상태가 된다")
    void deleteMarkWishlistDeleted() {
        Wishlist wishlist = Wishlist.create(1L);

        wishlist.delete();

        assertThat(wishlist.getStatus()).isEqualTo(WishlistStatus.DELETED);
        assertThat(wishlist.isActive()).isFalse();
        assertThat(wishlist.getDeletedAt()).isNotNull();
    }
}
