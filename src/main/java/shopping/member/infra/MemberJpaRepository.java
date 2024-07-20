package shopping.member.infra;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shopping.member.domain.Email;
import shopping.member.domain.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(Email email);

    Optional<Member> findByEmail(Email email);
}
