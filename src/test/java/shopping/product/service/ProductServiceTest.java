package shopping.product.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import shopping.infra.exception.ShoppingBusinessException;
import shopping.product.domain.Product;
import shopping.product.domain.ProductFixture;
import shopping.product.dto.ProductResponse;
import shopping.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks private ProductService productService;

    @Mock private ProductRepository productRepository;

    @Nested
    @DisplayName("상품을 조회할 때,")
    class getProduct {

        @Test
        @DisplayName("존재하지 않는 상품의 경우 예외가 발생합니다.")
        void invalidProduct() {
            // given
            final Long productId = 703L;

            given(productRepository.findProductByIdAndIsUse(productId, true))
                    .willThrow(new ShoppingBusinessException("상품이 존재하지 않습니다."));

            // when & then
            assertThatThrownBy(() -> productService.getProduct(productId))
                    .isInstanceOf(ShoppingBusinessException.class)
                    .hasMessage("상품이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("활성화 되어 있는 상품의 경우 성공적으로 조회합니다.")
        void success() {
            // given
            final Long productId = 703L;
            final String name = "ふろっこど～る なつめ";
            final BigDecimal price = BigDecimal.valueOf(1650L);
            final String imageUrl =
                    "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg";

            final Product product = ProductFixture.fixture(productId, name, price, imageUrl);

            given(productRepository.findProductByIdAndIsUse(productId, true))
                    .willReturn(Optional.of(product));

            // when
            final ProductResponse response = productService.getProduct(productId);

            // then
            assertSoftly(
                    it -> {
                        it.assertThat(response.productId()).isEqualTo(productId);
                        it.assertThat(response.name()).isEqualTo(name);
                        it.assertThat(response.price()).isEqualTo(price);
                        it.assertThat(response.imageUrl()).isEqualTo(imageUrl);
                    });
        }
    }
}
