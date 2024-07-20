package shopping.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.member.application.dto.MemberRequest;
import shopping.member.application.dto.MemberResponse;
import shopping.member.domain.Member;
import shopping.member.exception.MemberNotFoundException;
import shopping.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse save(final MemberRequest request) {
        final Member member = memberRepository.save(request.toEntity());

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
