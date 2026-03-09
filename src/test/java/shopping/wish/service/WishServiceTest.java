package shopping.wish.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.product.port.out.ProductSnapshot;
import shopping.product.port.out.ProductSnapshotProvider;
import shopping.wish.adapter.in.api.WishResponse;
import shopping.wish.domain.WishQuantity;
import shopping.wish.domain.Wishlist;
import shopping.wish.domain.WishlistItem;
import shopping.wish.domain.WishlistItemRepository;
import shopping.wish.domain.WishlistRepository;
import shopping.wish.domain.WishlistStatus;

@ExtendWith(MockitoExtension.class)
@DisplayName("[위시] 위시 서비스 조회 단위 테스트")
class WishServiceTest {
    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private WishlistItemRepository wishlistItemRepository;

    @Mock
    private ProductSnapshotProvider productSnapshotProvider;

    private WishService wishService;

    @BeforeEach
    void setUp() {
        wishService = new WishService(wishlistRepository, wishlistItemRepository, productSnapshotProvider);
    }

    @Test
    @DisplayName("삭제된 상품은 위시 목록에서 제외한다")
    void listSkipDeletedProductItems() {
        // given
        Long memberId = 1L;
        Wishlist wishlist = wishlist(memberId, 10L);
        WishlistItem validItem = wishlistItem(wishlist, 1L, 100L, 1);
        WishlistItem deletedProductItem = wishlistItem(wishlist, 2L, 200L, 1);
        ProductSnapshot activeProduct = productSnapshot(100L);

        when(wishlistRepository.findByMemberIdAndStatus(memberId, WishlistStatus.ACTIVE))
                .thenReturn(Optional.of(wishlist));
        when(wishlistItemRepository.findByWishlist_IdOrderByIdAsc(10L))
                .thenReturn(List.of(validItem, deletedProductItem));
        when(productSnapshotProvider.findActiveProduct(100L)).thenReturn(Optional.of(activeProduct));
        when(productSnapshotProvider.findActiveProduct(200L)).thenReturn(Optional.empty());

        // when
        List<WishResponse> responses = wishService.list(memberId);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).wishId()).isEqualTo(1L);
        assertThat(responses.get(0).productId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("상품 조회 중 다른 예외가 나면 그대로 전파한다")
    void listThrowWhenProductLookupFailsWithOtherError() {
        // given
        Long memberId = 1L;
        Wishlist wishlist = wishlist(memberId, 10L);
        WishlistItem item = wishlistItem(wishlist, 1L, 300L, 1);

        when(wishlistRepository.findByMemberIdAndStatus(memberId, WishlistStatus.ACTIVE))
                .thenReturn(Optional.of(wishlist));
        when(wishlistItemRepository.findByWishlist_IdOrderByIdAsc(10L)).thenReturn(List.of(item));
        when(productSnapshotProvider.findActiveProduct(300L)).thenThrow(new ApiException(ErrorCode.INTERNAL_ERROR));

        // when
        // then
        assertThatThrownBy(() -> wishService.list(memberId))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INTERNAL_ERROR);
    }

    private Wishlist wishlist(Long memberId, Long wishlistId) {
        Wishlist wishlist = Wishlist.create(memberId);
        setField(wishlist, "id", wishlistId);
        return wishlist;
    }

    private WishlistItem wishlistItem(
            Wishlist wishlist,
            Long wishId,
            Long productId,
            int quantity
    ) {
        WishlistItem item = WishlistItem.create(wishlist, productId, WishQuantity.from(quantity));
        setField(item, "id", wishId);
        return item;
    }

    private ProductSnapshot productSnapshot(Long productId) {
        return new ProductSnapshot(
                productId,
                "상품",
                new BigDecimal("10000"),
                "https://example.com/image.png"
        );
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to set field: " + fieldName, exception);
        }
    }
}
