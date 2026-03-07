package shopping.repository;

import org.springframework.data.repository.CrudRepository;
import shopping.domain.Wish;

public interface WishRepository extends CrudRepository<Wish, Long> {

}
