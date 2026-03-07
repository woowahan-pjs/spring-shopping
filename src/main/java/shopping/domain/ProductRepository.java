package shopping.domain;


import java.util.List;

public interface ProductRepository {

    Product save(Product product);

    Product findById(Long id);

    Product update(Long id, Product product);

    void deleteById(Long id);

    List<Product> findAll();
}
