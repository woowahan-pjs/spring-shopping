package shopping.infrastructure.persistence.member;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.domain.member.Member;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
