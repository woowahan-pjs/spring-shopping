package shopping.member.infra;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shopping.member.application.AlreadyRegisteredEmailException;
import shopping.member.application.MemberRepository;
import shopping.member.domain.Email;
import shopping.member.domain.Member;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    public MemberRepositoryImpl(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByEmail(Email email) {
        return memberJpaRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public void save(Member member) {
        try {
            memberJpaRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyRegisteredEmailException();
        }
    }
}
