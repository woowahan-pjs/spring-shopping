package shopping.wishlist.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shopping.product.domain.Price;
import shopping.product.domain.Product;
import shopping.product.domain.ProductFixture;

class WishListSaveContextTest {

    @Nested
    @DisplayName("이미 위시 리스트에 등록 된 항목인지 판단 할 때,")
    class isContainsItem {

        @Test
        @DisplayName("존재하지 않으면, false를 반환합니다.")
        void isFalse() {
            // given
            final Long productId = 99L;
            final WishListSaveContext context = createFixture();

            // when
            final boolean result = context.isContainsItem(productId);

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("이미 존재하는 경우, true를 반환합니다.")
        void isTrue() {
            // given
            final Long productId = 1L;
            final WishListSaveContext context = createFixture();

            // when
            final boolean result = context.isContainsItem(productId);

            // then
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("위시 리스트로 등록하고자 하는 상품이 유효하지 않은 상품인지 판단할 때,")
    class isNotContainsProductId {

        @Test
        @DisplayName("유효하지 않은 상품의 경우 true를 반환합니다.")
        void isTrue() {
            // given
            final Long productId = 3L;
            final WishListSaveContext context = createFixture();

            // when
            final boolean result = context.isNotContainsProductId(productId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("유효한 상품의 경우 false를 반환합니다.")
        void isFalse() {
            // given
            final Long productId = 1L;
            final WishListSaveContext context = createFixture();

            // when
            final boolean result = context.isNotContainsProductId(productId);

            // then
            assertThat(result).isFalse();
        }
    }

    private WishListSaveContext createFixture() {
        final Long wishListId = 1L;

        final Product product1 = ProductFixture.fixture(1L, "AAAAA", Price.create(3000L), "http://com");
        final Product product2 = ProductFixture.fixture(2L, "BBBBB", Price.create(3000L), "http://com");

        final WishList wishList = WishListFixture.fixture(wishListId, 1L,
            List.of(
                WishListItemFixture.fixture(1L, wishListId, product1)
            ));

        final List<Long> requestProductIds = List.of(1L, 2L, 3L);

        final List<Product> validProducts = List.of(product1, product2);

        return WishListSaveContext.create(wishList, requestProductIds, validProducts);
    }
}
