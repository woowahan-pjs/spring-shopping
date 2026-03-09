package shopping.product.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shopping.product.dto.ProductUpdateRequest;

class ProductTest {

    @Nested
    @DisplayName("상품 내용을 수정할 때,")
    class modify {

        @Test
        @DisplayName("기존 내용과 동일하면 내용을 수정하지 않습니다.")
        void notChange() {
            // given
            final Long productId = 1L;
            final String name = "AAAAA";
            final Price price = Price.create(3000L);
            final String imageUrl = "http://com";
            final Product product = ProductFixture.fixture(productId, name, price, imageUrl);

            final ProductUpdateRequest request = new ProductUpdateRequest(name, price, imageUrl);

            // when
            product.modify(request);

            // then
            assertSoftly(it -> {
                it.assertThat(product.getName()).isEqualTo(name);
                it.assertThat(product.getPrice()).isEqualTo(price);
                it.assertThat(product.getImageUrl()).isEqualTo(imageUrl);
            });
        }

        @Test
        @DisplayName("기존 내용과 다르면 내용을 수정합니다.")
        void success() {
            // given
            final Long productId = 1L;
            final String name = "AAAAA";
            final Price price = Price.create(3000L);
            final String imageUrl = "http://com";
            final Product product = ProductFixture.fixture(productId, "가나다라마", Price.create(100L), "https://com");

            final ProductUpdateRequest request = new ProductUpdateRequest(name, price, imageUrl);

            // when
            product.modify(request);

            // then
            assertSoftly(it -> {
                it.assertThat(product.getName()).isEqualTo(name);
                it.assertThat(product.getPrice()).isEqualTo(price);
                it.assertThat(product.getImageUrl()).isEqualTo(imageUrl);
            });
        }
    }
}