package shopping.member.application;

import java.util.Optional;
import shopping.member.domain.Email;
import shopping.member.domain.Member;

public interface MemberRepository {
    boolean existsByEmail(Email email);

    void save(Member member);

    Optional<Member> findByEmail(Email email);
}
