package shopping.product.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.common.exception.ProductNameInvalidException;
import shopping.product.application.command.ProductRegistrationCommand;
import shopping.product.application.query.ProductRegistrationQuery;
import shopping.product.domain.ProductValidator;
import shopping.product.domain.repository.ProductRepository;
import shopping.utils.fake.FakeProductRepository;
import shopping.utils.fixture.ProductFixture;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ProductRegistrationUseCaseTest {

    private ProductValidator productValidator;
    private ProductRepository productRepository;
    private ProductRegistrationUseCase productRegistrationUseCase;

    @BeforeEach
    void setUp() {
        productRepository = new FakeProductRepository();
        productValidator = new ProductValidator(word -> false);
        productRegistrationUseCase = new ProductService(productRepository, productValidator);
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void register() {
        final ProductRegistrationCommand command = new ProductRegistrationCommand(
                ProductFixture.NAME,
                ProductFixture.AMOUNT,
                ProductFixture.THUMBNAIL_IMAGE_URL,
                ProductFixture.SUB_CATEGORY_ID,
                ProductFixture.SHOP_ID,
                ProductFixture.SELLER_ID,
                ProductFixture.DETAILED_IMAGE_URLS
        );
        final ProductRegistrationQuery productRegistrationQuery = productRegistrationUseCase.register(command);

        assertAll(
                () -> assertThat(productRegistrationQuery).isNotNull(),
                () -> assertThat(productRegistrationQuery.id()).isNotNull(),
                () -> assertThat(productRegistrationQuery.name()).isEqualTo(ProductFixture.NAME),
                () -> assertThat(productRegistrationQuery.amount()).isEqualTo(ProductFixture.AMOUNT),
                () -> assertThat(productRegistrationQuery.thumbnailImageUrl()).isEqualTo(ProductFixture.THUMBNAIL_IMAGE_URL),
                () -> assertThat(productRegistrationQuery.subCategoryId()).isEqualTo(ProductFixture.SUB_CATEGORY_ID),
                () -> assertThat(productRegistrationQuery.shopId()).isEqualTo(ProductFixture.SHOP_ID),
                () -> assertThat(productRegistrationQuery.sellerId()).isEqualTo(ProductFixture.SELLER_ID),
                () -> assertThat(productRegistrationQuery.detailedImageUrls()).isEqualTo(ProductFixture.DETAILED_IMAGE_URLS)
        );
    }

    @DisplayName("상품 이름이 영문 욕설이 들었다면 상품을 등록할 수 없다")
    @Test
    void registerProductInvalidProductNameContainsProfanity() {
        productValidator = new ProductValidator(word -> true);
        productRegistrationUseCase = new ProductService(productRepository, productValidator);

        final ProductRegistrationCommand command = new ProductRegistrationCommand(
                ProductFixture.PROFANITY_NAME,
                ProductFixture.AMOUNT,
                ProductFixture.THUMBNAIL_IMAGE_URL,
                ProductFixture.SUB_CATEGORY_ID,
                ProductFixture.SHOP_ID,
                ProductFixture.SELLER_ID,
                ProductFixture.DETAILED_IMAGE_URLS
        );
        assertThatThrownBy(() -> productRegistrationUseCase.register(command))
                .isExactlyInstanceOf(ProductNameInvalidException.class);
    }
}
