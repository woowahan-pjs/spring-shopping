package shopping.product.service;

import shopping.product.domain.*;
import shopping.product.dto.ProductResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FindProductServiceTest {

    private InMemoryProductRepository productRepository;
    private ProductNameFactory nameFactory;
    private FindProductService service;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        nameFactory = new ProductNameFactory(new FakeProfanityChecker());
        service = new FindProductService(productRepository);
    }

    @Test
    void ID로_상품을_조회한다() {
        Product saved = productRepository
                .save(new Product(nameFactory.create("상품"), 1000, "http://img.png"));

        ProductResponse found = service.execute(saved.getId());

        assertEquals(saved.getId(), found.id());
        assertEquals("상품", found.name());
    }

    @Test
    void 존재하지_않는_ID로_조회하면_예외가_발생한다() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> service.execute(UUID.randomUUID()));

        assertEquals("상품이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    void 전체_상품을_조회한다() {
        productRepository.save(new Product(nameFactory.create("상품1"), 1000, "http://img1.png"));
        productRepository.save(new Product(nameFactory.create("상품2"), 2000, "http://img2.png"));

        List<ProductResponse> products = service.execute();

        assertEquals(2, products.size());
    }

    @Test
    void 상품이_없으면_빈_리스트를_반환한다() {
        List<ProductResponse> products = service.execute();

        assertTrue(products.isEmpty());
    }

    @Test
    void PENDING_상품은_조회되지_않는다() {
        productRepository.save(new Product(nameFactory.create("상품1"), 1000, "http://img1.png"));
        productRepository
                .save(new Product(nameFactory.createUnverified("상품2"), 2000, "http://img2.png"));

        List<ProductResponse> products = service.execute();

        assertEquals(1, products.size());
        assertEquals("상품1", products.get(0).name());
    }

    @Test
    void PENDING_상품은_ID로_조회되지_않는다() {
        Product saved = productRepository
                .save(new Product(nameFactory.createUnverified("상품"), 1000, "http://img.png"));

        assertThrows(NoSuchElementException.class, () -> service.execute(saved.getId()));
    }
}
