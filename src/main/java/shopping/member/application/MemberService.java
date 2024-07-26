package shopping.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.constant.enums.YesNo;
import shopping.exception.AuthorizationException;
import shopping.exception.BadRequestException;
import shopping.exception.NotFoundException;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;
import shopping.member.domain.Members;
import shopping.member.dto.MemberRequest;
import shopping.member.dto.MemberResponse;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Transactional
    public MemberResponse.MemberDetail createMember(MemberRequest.RegMember request) {
        checkEmailExists(request.getEmail());
        Member persistMember = memberRepository.save(request.toMember());
        return MemberResponse.MemberDetail.from(persistMember);
    }

    public MemberResponse.MembersRes findAllMembers() {
        Members members = findAllMembersToMembers();
        return MemberResponse.MembersRes.from(members);
    }



    public MemberResponse.MemberDetail findMemberDetailResponseBySn(Long sn) {
        Member persistMember = findMemberByMbrSn(sn);
        return MemberResponse.MemberDetail.from(persistMember);
    }

    public MemberResponse.ValidEmail validateNoneExistEmailToValidEmail(String email) {
        boolean isExists = isEmailExists(email);
        return MemberResponse.ValidEmail.of(isExists, email);
    }



    @Transactional
    public MemberResponse.MemberDetail updateMemberById(Long id, MemberRequest.ModMember request) {
        Member persistMember = findMemberByMbrSn(id);

        persistMember.updatePwdOrName(request.getPassword(), request.getName(), request.getNickName());
        return MemberResponse.MemberDetail.from(persistMember);
    }

    @Transactional
    public void deleteMemberById(Long id) {
        Member persistMember = findMemberByMbrSn(id);
        persistMember.updateDelYn(YesNo.Y);
    }




    private Member findMemberByMbrSn(Long sn) {
        return memberRepository.findById(sn)
                .orElseThrow(() -> new NotFoundException("해당 회원이 존재하지 않습니다."));
    }

    private boolean isEmailExists(String email) {
        return memberRepository.existsByEmail(email);
    }

    private void checkEmailExists(String email) {
        if(isEmailExists(email)) {
            throw new BadRequestException("이미 가입된 이메일 입니다.");
        }
    }

    private Members findAllMembersToMembers() {
        return new Members(memberRepository.findAll());
    }


    public boolean isExistMemberById(Long mbrSn) {
        return memberRepository.existsById(mbrSn);
    }

    public Member findMemberByEmailAndDelYn(String email, YesNo delYn) {
        Member member = Optional.ofNullable(memberRepository.findByEmailAndDelYn(email, delYn))
                .orElseThrow(() -> new AuthorizationException("해당 회원이 존재하지 않습니다."));
        return member;
    }
}
