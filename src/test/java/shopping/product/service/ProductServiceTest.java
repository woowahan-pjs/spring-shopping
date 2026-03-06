package shopping.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shopping.infra.client.purgomalum.PurgoMalumAdapter;
import shopping.infra.exception.ShoppingBusinessException;
import shopping.product.domain.NotFoundProductException;
import shopping.product.domain.Price;
import shopping.product.domain.Product;
import shopping.product.domain.ProductFixture;
import shopping.product.dto.ProductResponse;
import shopping.product.dto.ProductSaveRequest;
import shopping.product.dto.ProductSearchRequest;
import shopping.product.dto.ProductUpdateRequest;
import shopping.product.dto.ProductsSearchResponse;
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

            given(purgoMalumAdapter.isProfanity(eq(request.name()))).willReturn(true);

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

            given(purgoMalumAdapter.isProfanity(eq(request.name()))).willReturn(false);

            final Product expectedProduct =
                    ProductFixture.fixture(productId, name, price, imageUrl);
            given(productRepository.save(any(Product.class))).willReturn(expectedProduct);

            // when
            final Long result = productService.registerProduct(request);

            // then
            assertThat(result).isEqualTo(productId);
        }
    }

    @Nested
    @DisplayName("상품을 수정할 때,")
    class modifyProduct {

        @Test
        @DisplayName("상품이 존재하지 않는 경우 예외가 발생합니다.")
        void notFound() {
            // given
            final Long userId = 703L;
            final Long productId = 73L;
            final ProductUpdateRequest request = new ProductUpdateRequest(
                "ふろっこど～る なつめ",
                Price.create(1650L),
                "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg");

            given(productRepository.findProductByIdAndUserIdAndIsUse(productId, userId, true))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> productService.modifyProduct(userId, productId, request))
                .isInstanceOf(NotFoundProductException.class)
                .hasMessage("상품이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("변경할 상품명에 비속어가 포함된 경우 예외가 발생합니다.")
        void profanityIsTrue() {
            // given
            final Long userId = 703L;
            final Long productId = 73L;
            final ProductUpdateRequest request = new ProductUpdateRequest(
                "ふろっこど～る なつめ",
                Price.create(1650L),
                "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg");

            final Product expectedProduct = ProductFixture.fixture(productId, "안녕하세요", Price.create(3000L), "http://hello.world");
            given(productRepository.findProductByIdAndUserIdAndIsUse(productId, userId, true))
                .willReturn(Optional.of(expectedProduct));
            given(purgoMalumAdapter.isProfanity(any(String.class))).willReturn(true);

            // when & then
            assertThatThrownBy(() -> productService.modifyProduct(userId, productId, request))
                .isInstanceOf(ShoppingBusinessException.class)
                .hasMessage("상품명에 비속어가 포함되어 있습니다.");
        }

        @Test
        @DisplayName("성공적으로 상품 정보를 변경합니다.")
        void success() {
            // given
            final Long userId = 703L;
            final Long productId = 73L;
            final String name = "안녕하세요";
            final Price price = Price.create(3000L);
            final String imageUrl = "http://hello.world";
            final ProductUpdateRequest request = new ProductUpdateRequest(
                "ふろっこど～る なつめ",
                Price.create(1650L),
                "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg");

            final Product product = ProductFixture.fixture(productId, name, price, imageUrl);
            given(productRepository.findProductByIdAndUserIdAndIsUse(productId, userId, true))
                .willReturn(Optional.of(product));
            given(purgoMalumAdapter.isProfanity(any(String.class))).willReturn(false);

            // when
            productService.modifyProduct(userId, productId, request);

            // then
            assertSoftly(it -> {
                it.assertThat(product.getId()).isEqualTo(productId);
                it.assertThat(product.getName()).isEqualTo(name);
                it.assertThat(product.getPrice()).isEqualTo(price);
                it.assertThat(product.getImageUrl()).isEqualTo(imageUrl);
            });
        }
    }

    @Nested
    @DisplayName("상품을 삭제할 때,")
    class removeProduct {

        @Test
        @DisplayName("상품이 존재하지 않는 경우 예외가 발생합니다.")
        void notFound() {
            // given
            final Long userId = 703L;
            final Long productId = 73L;

            given(productRepository.findProductByIdAndUserIdAndIsUse(productId, userId, true))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> productService.removeProduct(userId, productId))
                .isInstanceOf(NotFoundProductException.class)
                .hasMessage("상품이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("성공적으로 상품을 삭제합니다.")
        void success() {
            // given
            final Long userId = 703L;
            final Long productId = 73L;

            final Product product = ProductFixture.fixture(productId, "안녕하세요", Price.create(3000L), "http://");
            given(productRepository.findProductByIdAndUserIdAndIsUse(productId, userId, true))
                .willReturn(Optional.of(product));

            // when
            productService.removeProduct(userId, productId);

            // then
            assertThat(product.getIsUse()).isFalse();
        }

        @Nested
        @DisplayName("상품을 검색할 때,")
        class searchProduct {

            @Test
            @DisplayName("성공적으로 조회합니다.")
            void success() {
                // given
                final Long productId = 1L;
                final String name = "안녕하세요";
                final Price price = Price.create(3500L);
                final String imageUrl = "http://com";
                final Pageable pageable = PageRequest.of(0, 20);
                final ProductSearchRequest request = new ProductSearchRequest(name, Price.create(3000L), Price.create(3500L));

                final List<Product> expectedProducts = List.of(ProductFixture.fixture(productId, name, price, imageUrl));
                given(productRepository.search(request.name(), request.fromPrice(), request.toPrice(), pageable))
                    .willReturn(new PageImpl<>(expectedProducts, pageable, expectedProducts.size()));

                // when
                final ProductsSearchResponse products = productService.searchProduct(request, pageable);
                final ProductResponse response = products.products().get(0);

                // then
                assertSoftly(it -> {
                    it.assertThat(response.productId()).isEqualTo(productId);
                    it.assertThat(response.name()).isEqualTo(name);
                    it.assertThat(response.price()).isEqualTo(price);
                    it.assertThat(response.imageUrl()).isEqualTo(imageUrl);
                });
            }
        }
    }
}
