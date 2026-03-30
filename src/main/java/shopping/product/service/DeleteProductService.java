package shopping.product.service;

import shopping.product.domain.*;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import shopping.wish.domain.RemoveWishByProduct;

@Transactional
public class DeleteProductService implements DeleteProduct {

    private final ProductRepository productRepository;
    private final RemoveWishByProduct removeWishByProduct;

    public DeleteProductService(ProductRepository productRepository,
            RemoveWishByProduct removeWishByProduct) {
        this.productRepository = productRepository;
        this.removeWishByProduct = removeWishByProduct;
    }

    @Override
    public void execute(UUID id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("상품이 존재하지 않습니다.");
        }
        productRepository.deleteById(id);
        removeWishByProduct.execute(id);
    }
}
