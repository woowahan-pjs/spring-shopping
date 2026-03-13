package shopping.product.infrastructure;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;

import java.util.List;
import java.util.NoSuchElementException;

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
    public Product findById(Long id) {
        return repository.findById(id)
                .map(ProductEntity::toDomain)
                .orElse(null);
    }

    @Override
    @Transactional
    public Product update(Long id, Product product) {
        ProductEntity entity = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다."));
        entity.update(product);
        return entity.toDomain();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll().stream()
                .map(ProductEntity::toDomain)
                .toList();
    }
}
