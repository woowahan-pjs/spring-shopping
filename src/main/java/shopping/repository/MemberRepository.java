package shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
}
