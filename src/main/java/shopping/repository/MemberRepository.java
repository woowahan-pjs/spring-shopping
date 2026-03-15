package shopping.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import shopping.domain.Member;

public interface MemberRepository extends CrudRepository<Member, Long> {

	Optional<Member> findByEmail(String email);
}
