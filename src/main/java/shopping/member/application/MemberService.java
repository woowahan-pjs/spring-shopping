package shopping.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;
import shopping.member.dto.MemberRequest;
import shopping.member.dto.MemberResponse;


@Service
@Transactional(readOnly = true)
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Transactional
    public MemberResponse.RegMemberResponse createMember(MemberRequest.RegMember request) {
        Member persistMember = memberRepository.save(request.toMember());
        return MemberResponse.RegMemberResponse.from(persistMember);
    }
}
