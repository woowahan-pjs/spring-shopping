package shopping.member.application;

import shopping.member.domain.Email;
import shopping.member.domain.Member;

public interface MemberRepository {
    boolean existsByEmail(Email email);

    void save(Member member);
}
