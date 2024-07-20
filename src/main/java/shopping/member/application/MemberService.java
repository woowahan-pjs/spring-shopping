package shopping.member.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.member.application.dto.MemberRequest;
import shopping.member.application.dto.MemberResponse;
import shopping.member.domain.Member;
import shopping.member.exception.InvalidMemberException;
import shopping.member.exception.MemberNotFoundException;
import shopping.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(final MemberRepository memberRepository, final PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Transactional
    public MemberResponse save(final MemberRequest request) {
        final String email = request.getEmail();
        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new InvalidMemberException("중복된 email 입니다.");
                });

        final String encodedPassword = passwordEncoder.encode(request.getPassword());
        final Member member = memberRepository.save(new Member(email, encodedPassword));

        return MemberResponse.from(member);
    }

    @Transactional
    public void delete(final Long id) {
        final Member member = findMemberById(id);

        memberRepository.delete(member);
    }

    private Member findMemberById(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));
    }

}
