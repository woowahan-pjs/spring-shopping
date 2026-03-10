package shopping.infrastructure.persistence.member;

import org.springframework.stereotype.Repository;
import shopping.domain.member.Member;
import shopping.domain.repository.MemberRepository;

import java.util.Optional;

@Repository
public class JpaMemberRepository implements MemberRepository {
    private final SpringDataJpaMemberRepository repository;

    public JpaMemberRepository(SpringDataJpaMemberRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Member save(Member member) {
        return repository.save(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
