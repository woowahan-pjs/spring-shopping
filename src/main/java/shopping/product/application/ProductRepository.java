package shopping.product.application;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shopping.product.domain.Product;

public interface ProductRepository {
    void save(Product product);

    Optional<Product> findById(Long id);

    Page<Product> findAllBy(Pageable pageable);

    void delete(Product product);
}
