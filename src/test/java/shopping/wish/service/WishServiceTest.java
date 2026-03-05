package shopping.wish.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.product.domain.Product;
import shopping.product.service.ProductService;
import shopping.wish.api.WishResponse;
import shopping.wish.domain.Wishlist;
import shopping.wish.domain.WishlistItem;
import shopping.wish.domain.WishlistItemRepository;
import shopping.wish.domain.WishlistRepository;
import shopping.wish.domain.WishlistStatus;

@ExtendWith(MockitoExtension.class)
class WishServiceTest {
    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private WishlistItemRepository wishlistItemRepository;

    @Mock
    private ProductService productService;

    private WishService wishService;

    @BeforeEach
    void setUp() {
        wishService = new WishService(wishlistRepository, wishlistItemRepository, productService);
    }

    @Test
    void listSkipDeletedProductItems() {
        Long memberId = 1L;
        Wishlist wishlist = Wishlist.create(memberId);
        setField(wishlist, "id", 10L);

        WishlistItem validItem = WishlistItem.create(wishlist, 100L, 1);
        setField(validItem, "id", 1L);
        setField(validItem, "addedAt", LocalDateTime.of(2026, 3, 5, 12, 0));

        WishlistItem deletedProductItem = WishlistItem.create(wishlist, 200L, 1);
        setField(deletedProductItem, "id", 2L);
        setField(deletedProductItem, "addedAt", LocalDateTime.of(2026, 3, 5, 12, 5));

        Product activeProduct = Product.create(
                "상품",
                "설명",
                "https://example.com/image.png",
                new BigDecimal("10000"),
                7L
        );
        setField(activeProduct, "id", 100L);

        when(wishlistRepository.findByMemberIdAndStatus(memberId, WishlistStatus.ACTIVE))
                .thenReturn(Optional.of(wishlist));
        when(wishlistItemRepository.findByWishlist_IdOrderByIdAsc(10L))
                .thenReturn(List.of(validItem, deletedProductItem));
        when(productService.findActive(100L)).thenReturn(activeProduct);
        when(productService.findActive(200L)).thenThrow(new ApiException(ErrorCode.PRODUCT_NOT_FOUND));

        List<WishResponse> responses = wishService.list(memberId);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).wishId()).isEqualTo(1L);
        assertThat(responses.get(0).productId()).isEqualTo(100L);
    }

    @Test
    void listThrowWhenProductLookupFailsWithOtherError() {
        Long memberId = 1L;
        Wishlist wishlist = Wishlist.create(memberId);
        setField(wishlist, "id", 10L);

        WishlistItem item = WishlistItem.create(wishlist, 300L, 1);
        setField(item, "id", 1L);
        setField(item, "addedAt", LocalDateTime.of(2026, 3, 5, 12, 0));

        when(wishlistRepository.findByMemberIdAndStatus(memberId, WishlistStatus.ACTIVE))
                .thenReturn(Optional.of(wishlist));
        when(wishlistItemRepository.findByWishlist_IdOrderByIdAsc(10L)).thenReturn(List.of(item));
        when(productService.findActive(300L)).thenThrow(new ApiException(ErrorCode.INTERNAL_ERROR));

        assertThatThrownBy(() -> wishService.list(memberId))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INTERNAL_ERROR);
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
