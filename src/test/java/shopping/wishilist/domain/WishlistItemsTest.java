package shopping.wishilist.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.product.domain.Product;
import shopping.product.fixture.ProductFixture;
import shopping.wishilist.exception.InvalidWishlistException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WishlistItemsTest {

    private WishlistItems wishlistItems;
    private Product 첫번째_상품;
    private Product 두번째_상품;

    @BeforeEach
    void setUp() {
        wishlistItems = new WishlistItems();
        첫번째_상품 = ProductFixture.createProduct(1L, "첫번째상품", "/path/image1", 100, 10_000);
        두번째_상품 = ProductFixture.createProduct(2L, "두번째상품", "/path/image2", 200, 20_000);
    }

    @Test
    @DisplayName("새로운 상품을 추가할 수 있다")
    void addProduct() {
        wishlistItems.add(첫번째_상품);

        assertThat(wishlistItems.getProducts()).containsExactly(첫번째_상품);
    }

    @Test
    @DisplayName("이미 존재하는 상품을 추가할 수 없다")
    void addDuplicateProduct() {
        wishlistItems.add(첫번째_상품);

        assertThatThrownBy(() -> wishlistItems.add(첫번째_상품))
                .isInstanceOf(InvalidWishlistException.class)
                .hasMessage("이미 존재하는 상품은 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("상품을 삭제할 수 있다")
    void removeProduct() {
        wishlistItems.add(첫번째_상품);
        wishlistItems.add(두번째_상품);

        wishlistItems.remove(첫번째_상품);

        assertThat(wishlistItems.getProducts()).containsExactly(두번째_상품);
    }

}
