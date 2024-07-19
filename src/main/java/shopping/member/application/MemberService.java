package shopping.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.constant.enums.YesNo;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void test() {
        Member member = Member.builder()
                .email("jtt@12cm.co.kr")
                .password("1234")
                .mbrNm("테스트")
                .delYn(YesNo.N)
                .build();

        memberRepository.save(member);

        List<Member> temp = memberRepository.findAll();

    }

}
