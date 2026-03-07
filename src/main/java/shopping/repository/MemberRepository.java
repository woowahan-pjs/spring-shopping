package shopping.repository;

import org.springframework.data.repository.CrudRepository;
import shopping.domain.Member;

public interface MemberRepository extends CrudRepository<Member, Long> {

}
