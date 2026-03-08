package shopping.service;

import shopping.domain.Member;
import shopping.domain.MemberRepository;

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
        };

        member.changePassword(passwordEncryptor.encrypt(member.getPassword()));

        repository.save(member);
    }
}
