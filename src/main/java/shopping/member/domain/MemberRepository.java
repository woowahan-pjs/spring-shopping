package shopping.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.constant.enums.YesNo;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Member findByEmailAndDelYn(String email, YesNo yesNo);
}
