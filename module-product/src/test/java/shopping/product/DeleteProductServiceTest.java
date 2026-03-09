package shopping.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteProductServiceTest {

    private InMemoryProductRepository productRepository;
    private DeleteProductService service;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        service = new DeleteProductService(productRepository);
    }

    @Test
    void 상품을_삭제한다() {
        Product saved =
                productRepository.save(new Product(new ProductName("상품"), 1000, "http://img.png"));

        service.execute(saved.getId());

        assertTrue(productRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void 존재하지_않는_상품을_삭제하면_예외가_발생한다() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> service.execute(UUID.randomUUID()));

        assertEquals("상품이 존재하지 않습니다.", exception.getMessage());
    }
}
