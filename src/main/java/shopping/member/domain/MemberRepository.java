package shopping.member.domain;

public interface MemberRepository {
    Member save(Member member);
    Member findByEmail(String email);
}
