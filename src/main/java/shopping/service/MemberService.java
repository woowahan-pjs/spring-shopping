package shopping.service;

import shopping.domain.Member;
import shopping.domain.MemberRepository;

public class MemberService {
    private final MemberRepository repository;
    private final EmailFormatValidator emailFormatValidator;

    public MemberService(MemberRepository repository, EmailFormatValidator emailFormatValidator) {
        this.repository = repository;
        this.emailFormatValidator = emailFormatValidator;
    }

    public void register(Member member) {
        if (!emailFormatValidator.isValid(member.getEmail())) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        };

        repository.save(member);
    }
}
