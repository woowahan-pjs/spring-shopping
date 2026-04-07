package shopping.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;
import shopping.member.domain.MemberRole;

import java.util.NoSuchElementException;

@Transactional
@Service
public class MemberService {
    private final MemberRepository repository;
    private final EmailFormatValidator emailFormatValidator;
    private final PasswordEncryptor passwordEncryptor;

    public MemberService(MemberRepository repository, EmailFormatValidator emailFormatValidator, PasswordEncryptor passwordEncryptor) {
        this.repository = repository;
        this.emailFormatValidator = emailFormatValidator;
        this.passwordEncryptor = passwordEncryptor;
    }

    public void register(Member member) {
        validationMember(member);

        member.changePassword(passwordEncryptor.encrypt(member.getPassword()));

        repository.save(member);
    }

    @Transactional(readOnly = true)
    public Member login(String email, String password) {
        Member member = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요"));

        if (!passwordEncryptor.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요");
        }

        return member;
    }

    public void adminRegister(Member member) {
        validationMember(member);

        member.changePassword(passwordEncryptor.encrypt(member.getPassword()));
        member.changeRole(MemberRole.ADMIN);

        repository.save(member);
    }

    @Transactional(readOnly = true)
    public Member findMember(Long memberId) {
        return repository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
    }

    private void validationMember(Member member) {
        if (!emailFormatValidator.isValid(member.getEmail())) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        if (repository.findByEmail(member.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }
}
