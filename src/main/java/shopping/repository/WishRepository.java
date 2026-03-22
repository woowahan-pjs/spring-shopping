package shopping.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import shopping.domain.Wish;

public interface WishRepository extends CrudRepository<Wish, Long> {

	List<Wish> findAllByMemberId(Long memberId);
}
