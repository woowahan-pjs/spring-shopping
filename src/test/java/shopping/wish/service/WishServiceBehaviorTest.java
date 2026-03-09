package shopping.wish.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;
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
import org.junit.jupiter.params.ParameterizedTest;

@ExtendWith(MockitoExtension.class)
class WishServiceBehaviorTest {
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

    @Nested
    class Add {
        @Test
        @DisplayName("수량이 없으면 기본 수량 1로 위시를 추가한다")
        void addWithDefaultQuantity() {
            // given
            Wishlist wishlist = wishlist(1L, 10L);
            ProductSnapshot product = createProductSnapshot(100L);
            when(wishlistRepository.findByMemberIdAndStatus(1L, WishlistStatus.ACTIVE))
                    .thenReturn(Optional.of(wishlist));
            when(wishlistItemRepository.existsByWishlist_IdAndProductId(10L, 100L)).thenReturn(false);
            when(productSnapshotProvider.getActiveProduct(100L)).thenReturn(product);
            when(wishlistItemRepository.save(any(WishlistItem.class))).thenAnswer(invocation -> {
                WishlistItem item = invocation.getArgument(0);
                assignWishlistItem(item, 1L);
                return item;
            });

            // when
            WishResponse response = wishService.add(1L, 100L, null);

            // then
            assertThat(response.quantity()).isEqualTo(1);
            assertThat(response.productId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("활성 위시리스트가 없으면 새로 만들고 위시를 추가한다")
        void addCreateWishlistWhenMissing() {
            // given
            ProductSnapshot product = createProductSnapshot(100L);
            when(wishlistRepository.findByMemberIdAndStatus(1L, WishlistStatus.ACTIVE))
                    .thenReturn(Optional.empty());
            when(wishlistRepository.save(any(Wishlist.class))).thenAnswer(invocation -> {
                Wishlist wishlist = invocation.getArgument(0);
                assignWishlist(wishlist, 10L);
                return wishlist;
            });
            when(wishlistItemRepository.existsByWishlist_IdAndProductId(10L, 100L)).thenReturn(false);
            when(productSnapshotProvider.getActiveProduct(100L)).thenReturn(product);
            when(wishlistItemRepository.save(any(WishlistItem.class))).thenAnswer(invocation -> {
                WishlistItem item = invocation.getArgument(0);
                assignWishlistItem(item, 1L);
                return item;
            });

            // when
            WishResponse response = wishService.add(1L, 100L, 2);

            // then
            assertThat(response.quantity()).isEqualTo(2);
            verify(wishlistRepository).save(any(Wishlist.class));
        }

        @Test
        @DisplayName("같은 상품이 이미 있으면 위시를 추가하지 않는다")
        void addThrowWhenWishAlreadyExists() {
            // given
            Wishlist wishlist = wishlist(1L, 10L);
            when(wishlistRepository.findByMemberIdAndStatus(1L, WishlistStatus.ACTIVE))
                    .thenReturn(Optional.of(wishlist));
            when(wishlistItemRepository.existsByWishlist_IdAndProductId(10L, 100L)).thenReturn(true);

            // when
            // then
            assertThatThrownBy(() -> wishService.add(1L, 100L, 1))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.WISH_ALREADY_EXISTS);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        @DisplayName("수량이 0 이하이면 위시를 추가하지 않는다")
        void addThrowWhenQuantityIsInvalid(int quantity) {
            // given
            Wishlist wishlist = wishlist(1L, 10L);
            ProductSnapshot product = createProductSnapshot(100L);
            when(wishlistRepository.findByMemberIdAndStatus(1L, WishlistStatus.ACTIVE))
                    .thenReturn(Optional.of(wishlist));
            when(wishlistItemRepository.existsByWishlist_IdAndProductId(10L, 100L)).thenReturn(false);
            when(productSnapshotProvider.getActiveProduct(100L)).thenReturn(product);

            // when
            // then
            assertThatThrownBy(() -> wishService.add(1L, 100L, quantity))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.WISH_QUANTITY_INVALID);
        }
    }

    @Nested
    class Delete {
        @Test
        @DisplayName("활성 위시 아이템이 있으면 삭제한다")
        void deleteSuccess() {
            // given
            Wishlist wishlist = wishlist(1L, 10L);
            WishlistItem item = WishlistItem.create(wishlist, 100L, WishQuantity.from(1));
            when(wishlistRepository.findByMemberIdAndStatus(1L, WishlistStatus.ACTIVE))
                    .thenReturn(Optional.of(wishlist));
            when(wishlistItemRepository.findByIdAndWishlist_Id(5L, 10L)).thenReturn(Optional.of(item));

            // when
            // then
            assertThatCode(() -> wishService.delete(1L, 5L)).doesNotThrowAnyException();

            verify(wishlistItemRepository).delete(item);
        }

        @Test
        @DisplayName("활성 위시리스트가 없으면 삭제하지 못한다")
        void deleteThrowWhenWishlistIsMissing() {
            // given
            when(wishlistRepository.findByMemberIdAndStatus(1L, WishlistStatus.ACTIVE)).thenReturn(Optional.empty());

            // when
            // then
            assertThatThrownBy(() -> wishService.delete(1L, 5L))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.WISHLIST_NOT_FOUND);
        }

        @Test
        @DisplayName("위시 아이템이 없으면 삭제하지 못한다")
        void deleteThrowWhenItemIsMissing() {
            // given
            Wishlist wishlist = wishlist(1L, 10L);
            when(wishlistRepository.findByMemberIdAndStatus(1L, WishlistStatus.ACTIVE))
                    .thenReturn(Optional.of(wishlist));
            when(wishlistItemRepository.findByIdAndWishlist_Id(5L, 10L)).thenReturn(Optional.empty());

            // when
            // then
            assertThatThrownBy(() -> wishService.delete(1L, 5L))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.WISH_ITEM_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("위시리스트가 없으면 빈 목록을 돌려준다")
    void listReturnEmptyWhenWishlistIsMissing() {
        // given
        when(wishlistRepository.findByMemberIdAndStatus(1L, WishlistStatus.ACTIVE)).thenReturn(Optional.empty());

        // when
        List<WishResponse> responses = wishService.list(1L);

        // then
        assertThat(responses).isEmpty();
    }

    private ProductSnapshot createProductSnapshot(Long productId) {
        return new ProductSnapshot(
                productId,
                "상품",
                new BigDecimal("10000"),
                "https://example.com/image.png"
        );
    }

    private Wishlist wishlist(Long memberId, Long wishlistId) {
        Wishlist wishlist = Wishlist.create(memberId);
        assignWishlist(wishlist, wishlistId);
        return wishlist;
    }

    private void assignWishlist(Wishlist wishlist, Long wishlistId) {
        setField(wishlist, "id", wishlistId);
    }

    private void assignWishlistItem(WishlistItem item, Long wishId) {
        setField(item, "id", wishId);
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
