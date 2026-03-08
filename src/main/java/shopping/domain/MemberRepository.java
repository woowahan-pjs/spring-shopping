package shopping.domain;

public interface MemberRepository {
    Member save(Member member);
    Member findById(Long id);
}
