package shopping.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import shopping.infra.client.purgomalum.PurgoMalumAdapter;
import shopping.infra.exception.ShoppingBusinessException;
import shopping.product.domain.Price;
import shopping.product.domain.Product;
import shopping.product.domain.ProductFixture;
import shopping.product.dto.ProductResponse;
import shopping.product.dto.ProductSaveRequest;
import shopping.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks private ProductService productService;

    @Mock private ProductRepository productRepository;

    @Mock private PurgoMalumAdapter purgoMalumAdapter;

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
            final Price price = Price.create(1650L);
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

    @Nested
    @DisplayName("상품을 등록할 때,")
    class registerProduct {

        @Test
        @DisplayName("비속어가 존재하면 예외가 발생합니다.")
        void profanityIsTrue() {
            // given
            final ProductSaveRequest request =
                    new ProductSaveRequest(
                            "ふろっこど～る なつめ",
                            Price.create(1650L),
                            "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg");

            given(purgoMalumAdapter.isProfanity(eq(request.name()))).willReturn(false);

            // when & then
            assertThatThrownBy(() -> productService.registerProduct(request))
                    .isInstanceOf(ShoppingBusinessException.class)
                    .hasMessage("상품명에 비속어가 포함되어 있습니다.");
        }

        @Test
        @DisplayName("성공적으로 상품을 등록합니다.")
        void success() {
            // given
            final Long productId = 703L;
            final String name = "ふろっこど～る なつめ";
            final Price price = Price.create(1650L);
            final String imageUrl =
                    "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg";
            final ProductSaveRequest request = new ProductSaveRequest(name, price, imageUrl);

            given(purgoMalumAdapter.isProfanity(eq(request.name()))).willReturn(true);

            final Product expectedProduct =
                    ProductFixture.fixture(productId, name, price, imageUrl);
            given(productRepository.save(any(Product.class))).willReturn(expectedProduct);

            // when
            final Long result = productService.registerProduct(request);

            // then
            assertThat(result).isEqualTo(productId);
        }
    }
}
