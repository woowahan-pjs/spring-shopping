package shopping.product.infra;

import org.springframework.stereotype.Repository;
import shopping.product.application.ProductRepository;
import shopping.product.domain.Product;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public void save(Product product) {
        productJpaRepository.save(product);
    }
}
