package shopping.wishlist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import shopping.product.domain.InMemoryProductRepository;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;
import shopping.wishlist.domain.InMemoryWishlistRepository;
import shopping.wishlist.domain.Wishlist;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@DisplayName("위시리스트 서비스")
class WishlistServiceTest {

    private WishlistService service;

    private PageRequest default_page = PageRequest.of(0, 20);

    @BeforeEach
    void setUp() {
        ProductRepository productRepository = new InMemoryProductRepository();
        service = new WishlistService(new InMemoryWishlistRepository(), productRepository);

        productRepository.save(new Product("피자", BigDecimal.ZERO, "http://a.com/a.jpg"));
    }

    @Test
    @DisplayName("위시리스트에 상품을 추가한다")
    void addWishlist() {
        Wishlist wishlist = new Wishlist(1L, 1L);

        service.addWishlist(wishlist);

        assertThat(wishlist.getId()).isNotNull();
    }

    @Test
    @DisplayName("이미 추가한 상품을 추가하면 예외발생")
    void addDuplicateWishlistItem() {
        Wishlist wishlist = new Wishlist(1L, 1L);

        service.addWishlist(wishlist);

        assertThatThrownBy(() -> service.addWishlist(wishlist))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않은 상품을 추가한다")
    void addNotExistProduct() {
        Wishlist wishlist = new Wishlist(1L, 2L);

        assertThatThrownBy(() -> service.addWishlist(wishlist))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("위시리스트를 조회한다.")
    void findAllByMemberId() {
        Wishlist wishlist = new Wishlist(1L, 1L);
        service.addWishlist(wishlist);

        Page<Wishlist> items = service.findWishlistItems(1L, default_page);

        assertThat(items.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("위시리스트 상품을 삭제한다")
    void deleteWishlistItem() {
        Wishlist wishlist = new Wishlist(1L, 1L);
        service.addWishlist(wishlist);

        service.deleteWishlistItem(wishlist.getMemberId(), wishlist.getId());

        Page<Wishlist> items = service.findWishlistItems(1L, default_page);

        assertThat(items).isEmpty();
    }

    @Test
    @DisplayName("위시리스트 존재하지 않으면 예외 발생")
    void deleteWishlistItem_invalidMemberId() {
        Wishlist wishlist = new Wishlist(1L, 1L);
        service.addWishlist(wishlist);

        assertThatThrownBy(() -> service.deleteWishlistItem(100L, wishlist.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}