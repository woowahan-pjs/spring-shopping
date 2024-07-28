package shopping.member.client.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import shopping.member.client.exception.DuplicateWishProductException;
import shopping.member.client.exception.NotFoundWishProductException;

@DisplayName("WishProducts")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WishProductsTest {

    @Test
    void WishProducts에_위시상품을_추가했다가_삭제할_수_있다() {
        final WishProducts wishProducts = new WishProducts();

        assertThatNoException().isThrownBy(() -> {
            wishProducts.wish(1L);
            wishProducts.unWish(1L);
        });
    }

    @Test
    void WishProducts에_위시상품을_중복해서_추가할_수_없다() {
        final WishProducts wishProducts = new WishProducts();
        wishProducts.wish(1L);

        assertThatThrownBy(() -> wishProducts.wish(1L))
                .isInstanceOf(DuplicateWishProductException.class);
    }

    @Test
    void WishProducts에_없는_위시상품을_삭제할_수_없다() {
        final WishProducts wishProducts = new WishProducts();

        assertThatThrownBy(() -> wishProducts.unWish(1L))
                .isInstanceOf(NotFoundWishProductException.class);
    }
}