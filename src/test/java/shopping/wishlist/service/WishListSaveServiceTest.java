package shopping.wishlist.service;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.any;
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
import shopping.product.repository.ProductRepository;
import shopping.wishlist.domain.WishList;
import shopping.wishlist.domain.WishListFixture;
import shopping.wishlist.domain.WishListItem;
import shopping.wishlist.domain.WishListItemFixture;
import shopping.wishlist.domain.WishListSaveStatus;
import shopping.wishlist.dto.WishListItemSaveRequest;
import shopping.wishlist.dto.WishListSaveRequest;
import shopping.wishlist.dto.WishListSaveSummary;
import shopping.wishlist.repository.WishListItemRepository;
import shopping.wishlist.repository.WishListRepository;

@ExtendWith(MockitoExtension.class)
class WishListSaveServiceTest {

    @InjectMocks
    private WishListSaveService wishListSaveService;

    @Mock
    private WishListRepository wishListRepository;

    @Mock
    private WishListItemRepository wishListItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Nested
    @DisplayName("위시 리스트 항목을 저장할 때,")
    class registerWishList {

        @Test
        @DisplayName("성공적으로 저장합니다.")
        void success() {
            // given
            final Long userId = 79L;
            final WishListSaveRequest request = new WishListSaveRequest(List.of(
                new WishListItemSaveRequest(1L),
                new WishListItemSaveRequest(2L),
                new WishListItemSaveRequest(3L)
            ));

            final int expectedFailCount = 2;
            final int expectedSuccessCount = 1;

            final WishList wishList = createFixture(userId);
            given(wishListRepository.findByWishList(userId))
                .willReturn(Optional.of(wishList));

            final Product product1 = createProductFixture1();
            final Product product2 = createProductFixture2();
            given(productRepository.findByIsUseAndIdIn(true, request.extractIds()))
                .willReturn(List.of(product1, product2));

            final WishListItem wishListItem = WishListItemFixture.fixture(1L, wishList.getId(), product2);
            given(wishListItemRepository.save(any(WishListItem.class))).willReturn(wishListItem);

            // when
            final WishListSaveSummary result = wishListSaveService.registerWishList(userId, request);

            // then
            assertSoftly(it -> {
                it.assertThat(result.getAllCount()).isEqualTo(request.extractIds().size());
                it.assertThat(result.getFailCount()).isEqualTo(expectedFailCount);
                it.assertThat(result.getSuccessCount()).isEqualTo(expectedSuccessCount);
                it.assertThat(result.getDetails()).filteredOn(detail -> detail.productId().equals(1L))
                    .singleElement()
                    .satisfies(detail -> assertSoftly(assertions -> {
                        assertions.assertThat(detail.wishId()).isNull();
                        assertions.assertThat(detail.status()).isEqualTo(WishListSaveStatus.FAILED);
                        assertions.assertThat(detail.message()).isEqualTo("이미 등록된 항목입니다.");
                    }));
                it.assertThat(result.getDetails()).filteredOn(detail -> detail.productId().equals(2L))
                    .singleElement()
                    .satisfies(detail -> assertSoftly(assertions -> {
                        assertions.assertThat(detail.wishId()).isNotNull();
                        assertions.assertThat(detail.status()).isEqualTo(WishListSaveStatus.SUCCEEDED);
                        assertions.assertThat(detail.message()).isEqualTo("저장에 성공하였습니다.");
                    }));
                it.assertThat(result.getDetails()).filteredOn(detail -> detail.productId().equals(3L))
                    .singleElement()
                    .satisfies(detail -> assertSoftly(assertions -> {
                        assertions.assertThat(detail.wishId()).isNull();
                        assertions.assertThat(detail.status()).isEqualTo(WishListSaveStatus.FAILED);
                        assertions.assertThat(detail.message()).isEqualTo("존재하지 않는 상품입니다.");
                    }));
            });
        }
    }

    private WishList createFixture(final Long userId) {
        final Long wishListId = 1L;

        return WishListFixture.fixture(wishListId, userId,
            List.of(
                WishListItemFixture.fixture(1L, wishListId, createProductFixture1())
            ));
    }

    private Product createProductFixture1() {
        return ProductFixture.fixture(1L, "AAAAA", Price.create(3000L), "http://com");
    }

    private Product createProductFixture2() {
        return ProductFixture.fixture(2L, "BBBBB", Price.create(3000L), "http://com");
    }
}
