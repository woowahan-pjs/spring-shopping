package shopping.domain;


public interface ProductRepository {

    Product save(Product product);

    Product findById(Long id);
}
