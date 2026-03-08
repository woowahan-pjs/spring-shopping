package shopping.domain;

public interface MemberRepository {
    Member save(Member member);
    Member findByEmail(String email);
}
