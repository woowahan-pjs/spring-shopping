package shopping.wishlist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import shopping.infra.exception.ShoppingBusinessException;
import shopping.product.domain.Price;
import shopping.product.domain.Product;
import shopping.product.domain.ProductFixture;
import shopping.wishlist.domain.WishList;
import shopping.wishlist.domain.WishListFixture;
import shopping.wishlist.domain.WishListItem;
import shopping.wishlist.domain.WishListItemFixture;
import shopping.wishlist.dto.WishListResponse;
import shopping.wishlist.repository.WishListRepository;

@ExtendWith(MockitoExtension.class)
class WishListServiceTest {

    @InjectMocks private WishListService wishListService;

    @Mock private WishListRepository wishListRepository;

    @Nested
    @DisplayName("위시 리스트를 조회할 때,")
    class getWishList {

        @Test
        @DisplayName("성공적으로 조회합니다.")
        void success() {
            // given
            final Long userId = 57L;

            final Long productId = 507L;
            final String productName = "고나나";
            final Price productPrice = Price.create(3000L);
            final String productImageUrl = "http://com";

            final Long wishListItemId = 1L;

            final Long wishListId = 79L;

            final Product expectedProduct =
                    ProductFixture.fixture(productId, productName, productPrice, productImageUrl);
            final WishListItem expectedItem =
                    WishListItemFixture.fixture(wishListItemId, wishListId, expectedProduct);
            final WishList expected =
                    WishListFixture.fixture(wishListId, userId, List.of(expectedItem));
            given(wishListRepository.findByWishList(userId)).willReturn(Optional.of(expected));

            // when
            final WishListResponse response = wishListService.getWishList(userId);

            // then
            assertSoftly(
                    it -> {
                        it.assertThat(response.userId()).isEqualTo(userId);
                        it.assertThat(response.items().getFirst().wishId())
                                .isEqualTo(wishListItemId);
                        it.assertThat(response.items().getFirst().name()).isEqualTo(productName);
                        it.assertThat(response.items().getFirst().price()).isEqualTo(productPrice);
                        it.assertThat(response.items().getFirst().imageUrl())
                                .isEqualTo(productImageUrl);
                    });
        }

        @Test
        @DisplayName("조회 결과가 없을 경우 빈 값을 반환한다.")
        void isNull() {
            // given
            final Long userId = 57L;

            given(wishListRepository.findByWishList(userId)).willReturn(Optional.empty());

            // when
            final WishListResponse response = wishListService.getWishList(userId);

            // then
            assertSoftly(
                    it -> {
                        it.assertThat(response.userId()).isEqualTo(userId);
                        it.assertThat(response.items()).isEmpty();
                    });
        }
    }

    @Nested
    @DisplayName("위시 리스트를 삭제할 때,")
    class removeWishList {

        @Test
        @DisplayName("등록된 위시 리스트 정보가 없으면 예외가 발생합니다.")
        void isNull() {
            // given
            final Long userId = 79L;
            final Long wishListItemId = 103L;

            given(wishListRepository.findByWishList(userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> wishListService.removeWishList(userId, wishListItemId))
                    .isInstanceOf(ShoppingBusinessException.class)
                    .hasMessage("등록된 위시 리스트가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("삭제하고자 하는 wishId가 없는 경우 예외가 발생합니다.")
        void notFoundItem() {
            // given
            final Long userId = 79L;
            final Long wishListItemId = 103L;

            final Long invalidWishListItemId = 30L;

            final Product expectedProduct =
                    ProductFixture.fixture(13L, "고나나", Price.create(1300L), "http://com");
            final WishListItem expectedItem =
                    WishListItemFixture.fixture(1L, invalidWishListItemId, expectedProduct);
            final WishList expected = WishListFixture.fixture(1L, userId, List.of(expectedItem));
            given(wishListRepository.findByWishList(userId)).willReturn(Optional.of(expected));

            // when & then
            assertThatThrownBy(() -> wishListService.removeWishList(userId, wishListItemId))
                    .isInstanceOf(ShoppingBusinessException.class)
                    .hasMessage("존재하지 않는 위시 리스트 입니다.");
        }

        @Test
        @DisplayName("성공적으로 삭제합니다.")
        void success() {
            // given
            final Long userId = 79L;
            final Long wishListItemId = 103L;

            final Product expectedProduct =
                    ProductFixture.fixture(13L, "고나나", Price.create(1300L), "http://com");
            final WishListItem expectedItem =
                    WishListItemFixture.fixture(1L, wishListItemId, expectedProduct);
            final WishList expected = WishListFixture.fixture(1L, userId, List.of(expectedItem));
            given(wishListRepository.findByWishList(userId)).willReturn(Optional.of(expected));

            // when
            wishListService.removeWishList(userId, wishListItemId);

            // then
            assertThat(expectedItem.getIsUse()).isFalse();
        }
    }
}
