package shopping.member.service;

import org.springframework.stereotype.Service;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;

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
        if (!emailFormatValidator.isValid(member.getEmail())) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        if (repository.findByEmail(member.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        member.changePassword(passwordEncryptor.encrypt(member.getPassword()));

        repository.save(member);
    }

    public Member login(String email, String password) {
        Member member = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요"));

        if (!passwordEncryptor.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요");
        }

        return member;
    }
}
