package shopping.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.domain.*;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class WishlistItemServiceTest {

    private WishlistItemService service;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
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
    @DisplayName("중복 상품을 추가하면 예외발생")
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
}