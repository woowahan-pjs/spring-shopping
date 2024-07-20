package shopping.wishilist.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.product.application.ProductProvider;
import shopping.product.domain.Product;
import shopping.wishilist.WishlistRepository;
import shopping.wishilist.application.dto.WishlistRequest;
import shopping.wishilist.application.dto.WishlistResponse;
import shopping.wishilist.domain.Wishlist;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WishlistServiceTest {

    private static final Long MEMBER_ID = 1L;

    private WishlistService wishlistService;
    private WishlistRepository wishlistRepository;
    private ProductProvider productProvider;

    @BeforeEach
    void setUp() {
        wishlistRepository = mock(WishlistRepository.class);
        productProvider = mock(ProductProvider.class);
        wishlistService = new WishlistService(wishlistRepository, productProvider);
    }

    @Test
    @DisplayName("회원의 위시리스트에 상품을 추가할 수 있다")
    void addProductToWishlist() {
        final Long productId = 1L;
        final WishlistRequest request = new WishlistRequest(productId);
        final Product product = new Product("첫번째상품", "/path/image1", 100, 10_000);

        final Wishlist wishlist = new Wishlist(MEMBER_ID);
        when(wishlistRepository.findByMemberId(anyLong())).thenReturn(Optional.of(wishlist));
        when(productProvider.findProductById(anyLong())).thenReturn(product);

        final WishlistResponse response = wishlistService.addProduct(MEMBER_ID, request);

        assertThat(response.getProducts()).extracting("name").containsExactly("첫번째상품");
    }

    @Test
    @DisplayName("회원의 위시리스트가 없으면 생성하고 상품을 추가할 수 있다")
    void createWishlistAndAddProduct() {
        final Long productId = 1L;
        final WishlistRequest request = new WishlistRequest(productId);
        final Product product = new Product("첫번째상품", "/path/image1", 100, 10_000);

        when(wishlistRepository.findByMemberId(anyLong())).thenReturn(Optional.empty());
        when(productProvider.findProductById(anyLong())).thenReturn(product);

        final WishlistResponse response = wishlistService.addProduct(MEMBER_ID, request);

        assertThat(response.getProducts()).extracting("name").containsExactly("첫번째상품");
    }

    @Test
    @DisplayName("회원의 위시리스트에서 상품을 제거할 수 있다")
    void removeProductFromWishlist() {
        final Long productId = 1L;
        final Product product = new Product("첫번째상품", "/path/image1", 100, 10_000);

        final Wishlist wishlist = new Wishlist(MEMBER_ID);
        wishlist.add(product);
        when(wishlistRepository.findByMemberId(anyLong())).thenReturn(Optional.of(wishlist));
        when(productProvider.findProductById(anyLong())).thenReturn(product);

        wishlistService.deleteProduct(MEMBER_ID, productId);

        assertThat(wishlist.getWishlistItems()).doesNotContain(product);
    }

    @Test
    @DisplayName("회원의 위시리스트를 조회할 수 있다")
    void findOrCreateWishlistByMember() {
        final Wishlist wishlist = new Wishlist(MEMBER_ID);
        when(wishlistRepository.findByMemberId(anyLong())).thenReturn(Optional.of(wishlist));

        final WishlistResponse response = wishlistService.findOrCreateByMember(MEMBER_ID);

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("회원의 위시리스트 조회 시 없는 경우 새로 생성되어 조회된다")
    void createWishlistIfNotExists() {
        when(wishlistRepository.findByMemberId(anyLong())).thenReturn(Optional.empty());

        final WishlistResponse response = wishlistService.findOrCreateByMember(MEMBER_ID);

        assertThat(response).isNotNull();
    }
}
