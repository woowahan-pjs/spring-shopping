package shopping.member.domain;

import java.util.Optional;

public interface MemberRepository {
    MemberEntity save(MemberEntity member);
    Optional<MemberEntity> findByEmail(String email);
}
