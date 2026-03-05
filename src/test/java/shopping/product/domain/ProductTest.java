package shopping.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    @DisplayName("상품을 생성할 수 있다")
    void test01() {
        // when
        Product product = Product.builder()
                                 .name("상품명")
                                 .price(10000L)
                                 .imageUrl("https://example.com/image.jpg")
                                 .build();

        // then
        assertThat(product.getName()).isEqualTo("상품명");
        assertThat(product.getPrice()).isEqualTo(10000L);
        assertThat(product.getImageUrl()).isEqualTo("https://example.com/image.jpg");
    }

    @Test
    @DisplayName("상품 정보를 수정할 수 있다")
    void test02() {
        // given
        Product product = Product.builder()
                                 .name("상품명")
                                 .price(10000L)
                                 .imageUrl("https://example.com/image.jpg")
                                 .build();

        // when
        product.update("수정된 상품명", 20000L, "https://example.com/new-image.jpg");

        // then
        assertThat(product.getName()).isEqualTo("수정된 상품명");
        assertThat(product.getPrice()).isEqualTo(20000L);
        assertThat(product.getImageUrl()).isEqualTo("https://example.com/new-image.jpg");
    }
}
