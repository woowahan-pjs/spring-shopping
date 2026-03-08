package shopping.wishlist.service;

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
}
