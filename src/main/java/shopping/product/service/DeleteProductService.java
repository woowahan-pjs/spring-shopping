package shopping.product.service;

import shopping.product.domain.*;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DeleteProductService implements DeleteProduct {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    public DeleteProductService(ProductRepository productRepository,
            ApplicationEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(UUID id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("상품이 존재하지 않습니다.");
        }
        productRepository.deleteById(id);
        eventPublisher.publishEvent(new ProductDeletedEvent(id));
    }
}
