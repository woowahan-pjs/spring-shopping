package shopping.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
