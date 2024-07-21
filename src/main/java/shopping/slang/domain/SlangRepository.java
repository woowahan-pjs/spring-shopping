package shopping.slang.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.product.domain.Product;

public interface SlangRepository extends JpaRepository<Slang, Long> {

}
