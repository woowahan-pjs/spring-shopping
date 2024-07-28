package shopping.member.common.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.member.common.domain.Member;
import shopping.member.common.domain.MemberRepository;

public interface JPAMemberRepository extends MemberRepository, JpaRepository<Member, String> {

}
