package shopping.product.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;

import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository repository;

    public ProductRepositoryImpl(ProductJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product save(Product product) {
        return repository.save(ProductEntity.from(product)).toDomain();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id)
                .map(ProductEntity::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(ProductEntity::toDomain);
    }
}
