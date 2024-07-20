package shopping.product.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.product.application.dto.ProductCreateRequest;
import shopping.product.application.dto.ProductModifyRequest;
import shopping.product.application.dto.ProductResponse;
import shopping.product.domain.Product;
import shopping.product.exception.InvalidProductException;
import shopping.product.exception.ProductNotExistException;
import shopping.product.infra.ProfanityChecker;
import shopping.product.repository.ProductRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProfanityChecker profanityChecker;

    @Test
    @DisplayName("존재하지 않는 상품 ID로 조회하면 예외가 발생한다")
    void findByIdWhenProductNotExist() {
        final Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(productId))
                .isInstanceOf(ProductNotExistException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("상품을 등록할 수 있다")
    void save() {
        final ProductCreateRequest createRequest = createProductCreateRequest("productName");
        final Product product = createProduct();
        when(profanityChecker.containsProfanity(createRequest.getName())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        final ProductResponse response = productService.save(createRequest);

        assertThat(response.getName()).isEqualTo("productName");
    }

    @Test
    @DisplayName("상품 이름에 비속어가 포함되어 있으면 등록시 예외가 발생한다")
    void saveWhenNameContainsProfanity() {
        final ProductCreateRequest createRequest = createProductCreateRequest("병신");
        when(profanityChecker.containsProfanity(createRequest.getName())).thenReturn(true);

        assertThatThrownBy(() -> productService.save(createRequest))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("상품이름에는 비속어를 포함할 수 없습니다.");
    }

    @Test
    @DisplayName("상품을 수정할 수 있다")
    void update() {
        final Long productId = 1L;
        final ProductModifyRequest modifyRequest = createProductModifyRequest("newProductName");
        final Product product = createProduct();
        when(profanityChecker.containsProfanity(modifyRequest.getName())).thenReturn(false);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.update(productId, modifyRequest);

        assertSoftly(softly -> {
            softly.assertThat(product.getName()).isEqualTo("newProductName");
            softly.assertThat(product.getImagePath()).isEqualTo("/image/path2");
            softly.assertThat(product.getAmount()).isEqualTo(20);
            softly.assertThat(product.getPrice()).isEqualTo(2000);
        });
    }

    @Test
    @DisplayName("존재하지 않는 상품을 수정하려 하면 예외가 발생한다")
    void updateWhenProductNotExist() {
        final Long productId = 1L;
        final ProductModifyRequest modifyRequest = createProductModifyRequest("newProductName");
        when(profanityChecker.containsProfanity(modifyRequest.getName())).thenReturn(false);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.update(productId, modifyRequest))
                .isInstanceOf(ProductNotExistException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("상품 이름에 비속어가 포함되어 있으면 수정시 예외가 발생한다")
    void updateWhenNameContainsProfanity() {
        final ProductModifyRequest modifyRequest = createProductModifyRequest("병신");
        when(profanityChecker.containsProfanity(modifyRequest.getName())).thenReturn(true);

        assertThatThrownBy(() -> productService.update(1L, modifyRequest))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("상품이름에는 비속어를 포함할 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 상품을 삭제하려 하면 예외가 발생한다")
    void deleteWhenProductNotExist() {
        final Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.delete(productId))
                .isInstanceOf(ProductNotExistException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    private ProductCreateRequest createProductCreateRequest(final String productName) {
        return new ProductCreateRequest(productName, "/image/path", 10, 1000);
    }

    private static ProductModifyRequest createProductModifyRequest(final String newProductName) {
        return new ProductModifyRequest(newProductName, "/image/path2", 20, 2000);
    }

    private Product createProduct() {
        return new Product("productName", "/image/path", 10, 1000);
    }
}
