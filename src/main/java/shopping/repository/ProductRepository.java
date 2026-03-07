package shopping.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import shopping.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
	List<Product> findAll();
}
