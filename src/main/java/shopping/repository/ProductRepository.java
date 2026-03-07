package shopping.repository;

import org.springframework.data.repository.CrudRepository;
import shopping.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
