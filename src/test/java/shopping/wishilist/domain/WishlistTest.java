package shopping.wishilist.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.wishilist.exception.InvalidWishlistException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WishlistTest {
    @Test
    @DisplayName("Wishlist 는 memberId를 필수값으로 가진다")
    void wishlistRequiresMemberId() {
        assertThatThrownBy(() -> new Wishlist(null))
                .isInstanceOf(InvalidWishlistException.class)
                .hasMessage("wishlist 생성시 memberId 는 필수값입니다.");
    }
}
