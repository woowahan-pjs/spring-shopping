package shopping.infrastructure.persistence.member;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.domain.member.Member;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
}
