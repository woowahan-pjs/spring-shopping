package shopping.product.domain;


import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    Product update(Long id, Product product);

    void deleteById(Long id);

    List<Product> findAll();
}
