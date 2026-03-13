package shopping.member.infrastructure;

import org.springframework.stereotype.Repository;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;

import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository repository;

    public MemberRepositoryImpl(MemberJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Member save(Member member) {
        return repository.save(MemberEntity.from(member)).toDomain();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(MemberEntity::toDomain);
    }
}
