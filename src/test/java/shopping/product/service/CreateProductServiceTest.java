package shopping.product.service;

import shopping.product.domain.*;
import shopping.product.dto.CreateProductResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateProductServiceTest {

    private InMemoryProductRepository productRepository;
    private CreateProductService service;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        ProductNameFactory nameFactory = new ProductNameFactory(new FakeProfanityChecker());
        SaveProductService saveProductService = new SaveProductService(productRepository);
        service = new CreateProductService(nameFactory, saveProductService);
    }

    @Test
    void 상품을_생성하면_ID를_반환한다() {
        CreateProductResponse response = service.execute("상품", 1000, "http://image.png");

        assertNotNull(response.id());
    }

    @Test
    void 생성된_상품이_저장소에_저장된다() {
        CreateProductResponse response = service.execute("상품", 1000, "http://image.png");

        Product found = productRepository.findById(response.id()).orElseThrow();
        assertEquals("상품", found.getName().getValue());
        assertEquals(1000, found.getPrice());
        assertEquals("http://image.png", found.getImageUrl());
        assertEquals(ProductStatus.CREATED, found.getStatus());
    }

    @Test
    void 유효하지_않은_이름이면_예외가_발생한다() {
        assertThrows(IllegalArgumentException.class,
                () -> service.execute("", 1000, "http://image.png"));
    }

    @Test
    void 비속어가_포함되면_예외가_발생한다() {
        ProductNameFactory nameFactory =
                new ProductNameFactory(new FakeProfanityChecker("badword"));
        SaveProductService saveProductService = new SaveProductService(productRepository);
        CreateProductService serviceWithProfanity =
                new CreateProductService(nameFactory, saveProductService);

        assertThrows(IllegalArgumentException.class,
                () -> serviceWithProfanity.execute("badword", 1000, "http://image.png"));
    }

    @Test
    void 외부_API_실패시_PENDING_상태로_저장된다() {
        ProductNameFactory nameFactory = new ProductNameFactory(text -> {
            throw new RuntimeException("API failure");
        });
        SaveProductService saveProductService = new SaveProductService(productRepository);
        CreateProductService serviceWithFailure =
                new CreateProductService(nameFactory, saveProductService);

        CreateProductResponse response = serviceWithFailure.execute("상품", 1000, "http://image.png");

        Product found = productRepository.findById(response.id()).orElseThrow();
        assertEquals(ProductStatus.PENDING, found.getStatus());
    }
}
