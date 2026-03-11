package shopping.product.service;

import shopping.product.domain.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteProductServiceTest {

    private InMemoryProductRepository productRepository;
    private ProductNameFactory nameFactory;
    private List<Object> publishedEvents;
    private DeleteProductService service;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        nameFactory = new ProductNameFactory(new FakeProfanityChecker());
        publishedEvents = new ArrayList<>();
        service = new DeleteProductService(productRepository, publishedEvents::add);
    }

    @Test
    void 상품을_삭제한다() {
        Product saved = productRepository
                .save(new Product(nameFactory.create("상품"), 1000, "http://img.png"));

        service.execute(saved.getId());

        assertTrue(productRepository.findById(saved.getId()).isEmpty());
        assertEquals(1, publishedEvents.size());
        assertEquals(saved.getId(), ((ProductDeletedEvent) publishedEvents.get(0)).productId());
    }

    @Test
    void 존재하지_않는_상품을_삭제하면_예외가_발생한다() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> service.execute(UUID.randomUUID()));

        assertEquals("상품이 존재하지 않습니다.", exception.getMessage());
    }
}
