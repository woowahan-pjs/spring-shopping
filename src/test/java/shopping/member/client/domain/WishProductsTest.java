package shopping.member.client.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import shopping.member.client.exception.NotFoundWishProductException;

@DisplayName("WishProducts")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WishProductsTest {

    @Test
    void WishProducts에_위시상품을_추가했다가_삭제할_수_있다() {
        final WishProducts wishProducts = new WishProducts();
        final WishProduct wishProduct = new WishProduct(1L);

        assertThatNoException().isThrownBy(() -> {
            wishProducts.wish(wishProduct);
            wishProducts.unWish(wishProduct);
        });
    }

    @Test
    void WishProducts에_위시상품을_중복해서_추가할_수_없다() {
        final WishProducts wishProducts = new WishProducts();
        final WishProduct wishProduct = new WishProduct(1L);
        wishProducts.wish(wishProduct);

        assertThatThrownBy(() -> wishProducts.wish(wishProduct))
                .isInstanceOf(DuplicateWishProductException.class);
    }

    @Test
    void WishProducts에_없는_위시상품을_삭제할_수_없다() {
        final WishProducts wishProducts = new WishProducts();
        final WishProduct wishProduct = new WishProduct(1L);

        assertThatThrownBy(() -> wishProducts.unWish(wishProduct))
                .isInstanceOf(NotFoundWishProductException.class);
    }
}