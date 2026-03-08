package shopping.wishlist.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import shopping.infra.exception.ShoppingBusinessException;
import shopping.product.domain.Price;
import shopping.product.domain.ProductFixture;

class WishListTest {

    @Nested
    @DisplayName("wishListItem를 삭제할 때,")
    class removeItem {

        @Test
        @DisplayName("wishListItemId에 해당하는 항목을 찾지 못하면 예외가 발생합니다.")
        void notFoundItem() {
            // given
            final Long targetWishListItemId = 99L;

            final Long wishListId = 1L;
            final WishListItem item =
                    WishListItemFixture.fixture(
                            2L,
                            wishListId,
                            ProductFixture.fixture(1L, "고나나", Price.create(3000L), "http://com"));
            final WishList wishList = WishListFixture.fixture(wishListId, 79L, List.of(item));

            // when & then
            assertThatThrownBy(() -> wishList.removeItem(targetWishListItemId))
                    .isInstanceOf(ShoppingBusinessException.class)
                    .hasMessage("존재하지 않는 위시 리스트 입니다.");
        }

        @Test
        @DisplayName("성공적으로 삭제합니다.")
        void success() {
            // given
            final Long wishListItemId = 99L;

            final Long wishListId = 1L;
            final WishListItem item =
                    WishListItemFixture.fixture(
                            wishListItemId,
                            wishListId,
                            ProductFixture.fixture(1L, "고나나", Price.create(3000L), "http://com"));
            final WishList wishList = WishListFixture.fixture(wishListId, 79L, List.of(item));

            // when
            wishList.removeItem(wishListItemId);

            // then
            assertThat(wishList.getItems().getFirst().getIsUse()).isFalse();
        }
    }
}
