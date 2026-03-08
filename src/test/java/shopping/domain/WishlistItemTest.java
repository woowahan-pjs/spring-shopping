package shopping.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static shopping.domain.ProductFixture.*;

public class WishlistItemTest {

    @Test
    @DisplayName("위시리스트에 상품을 추가한다.")
    void addWishlistItem() {
        Member member = createMember();
        Product product = createProduct();
        member.assignId(1L);
        product.assignId(1L);

        WishlistItem wishlistItem = new WishlistItem(member.getId(), product.getId());

        assertAll(
            () -> assertThat(wishlistItem.getMemberId()).isEqualTo(member.getId()),
            () -> assertThat(wishlistItem.getProductId()).isEqualTo(product.getId())
        );
    }

    @Test
    @DisplayName("회원 아이디가 없으면 예외가 발생한다.")
    void invalidMemberId() {
        Product product = createProduct();

        assertThatThrownBy(() -> new WishlistItem(null, product.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 아이디가 없으면 예외가 발생한다.")
    void invalidProductId() {
        Member member = createMember();

        assertThatThrownBy(() -> new WishlistItem(member.getId(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Member createMember() {
        return new Member("test@gmail.com", "password");
    }
}
