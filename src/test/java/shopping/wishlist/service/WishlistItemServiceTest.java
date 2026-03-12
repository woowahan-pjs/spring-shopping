package shopping.wishlist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.product.domain.InMemoryProductRepository;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;
import shopping.wishlist.domain.InMemoryWishlistItemRepository;
import shopping.wishlist.domain.WishlistItem;
import shopping.wishlist.service.WishlistItemService;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class WishlistItemServiceTest {

    private WishlistItemService service;

    @BeforeEach
    void setUp() {
        ProductRepository productRepository = new InMemoryProductRepository();
        service = new WishlistItemService(new InMemoryWishlistItemRepository(), productRepository);

        productRepository.save(new Product("피자", BigDecimal.ZERO, "http://a.com/a.jpg"));
    }

    @Test
    @DisplayName("위시리스트에 상품을 추가한다")
    void addWishlistItem() {
        WishlistItem wishlistItem = new WishlistItem(1L, 1L);

        service.addWishlistItem(wishlistItem);

        assertThat(wishlistItem.getId()).isNotNull();
    }

    @Test
    @DisplayName("이미 추가한 상품을 추가하면 예외발생")
    void addDuplicateWishlistItem() {
        WishlistItem wishlistItem = new WishlistItem(1L, 1L);

        service.addWishlistItem(wishlistItem);

        assertThatThrownBy(() -> service.addWishlistItem(wishlistItem))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않은 상품을 추가한다")
    void addNotExistProduct() {
        WishlistItem wishlistItem = new WishlistItem(1L, 2L);

        assertThatThrownBy(() -> service.addWishlistItem(wishlistItem))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("위시리스트를 조회한다.")
    void findAllByMemberId() {
        WishlistItem wishlistItem = new WishlistItem(1L, 1L);
        service.addWishlistItem(wishlistItem);

        List<WishlistItem> items = service.findWishlistItems(1L);

        assertThat(items.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("위시리스트 상품을 삭제한다")
    void deleteWishlistItem() {
        WishlistItem wishlistItem = new WishlistItem(1L, 1L);
        service.addWishlistItem(wishlistItem);

        service.deleteWishlistItem(wishlistItem.getMemberId(), wishlistItem.getId());

        List<WishlistItem> items = service.findWishlistItems(1L);

        assertThat(items).isEmpty();
    }

    @Test
    @DisplayName("위시리스트 존재하지 않으면 예외 발생")
    void deleteWishlistItem_invalidMemberId() {
        WishlistItem wishlistItem = new WishlistItem(1L, 1L);
        service.addWishlistItem(wishlistItem);

        assertThatThrownBy(() -> service.deleteWishlistItem(100L, wishlistItem.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}