package shopping.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.exception.NotFoundException;
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
    public MemberResponse.MemberDetail createMember(MemberRequest.RegMember request) {
        Member persistMember = memberRepository.save(request.toMember());
        return MemberResponse.MemberDetail.from(persistMember);
    }

    public MemberResponse.MemberDetail findMemberDetailResponseBySn(Long sn) {
        Member persistMember = findMemberByMbrSn(sn);
        return MemberResponse.MemberDetail.from(persistMember);
    }

    @Transactional
    public MemberResponse.MemberDetail updateMember(Long id, MemberRequest.ModMember request) {
        Member persistMember = findMemberByMbrSn(id);

        persistMember.updatePwdOrName(request.getPassword(), request.getName());
        return MemberResponse.MemberDetail.from(persistMember);
    }




    private Member findMemberByMbrSn(Long sn) {
        return memberRepository.findById(sn)
                .orElseThrow(() -> new NotFoundException("해당 회원이 존재하지 않습니다."));
    }
}
