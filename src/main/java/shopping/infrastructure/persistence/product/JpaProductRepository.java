package shopping.infrastructure.persistence.product;

import org.springframework.stereotype.Repository;
import shopping.domain.product.Product;
import shopping.domain.repository.ProductRepository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class JpaProductRepository implements ProductRepository {
    private final SpringDataJpaProductRepository repository;

    public JpaProductRepository(SpringDataJpaProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Collection<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Product product) {
        repository.delete(product);
    }
}