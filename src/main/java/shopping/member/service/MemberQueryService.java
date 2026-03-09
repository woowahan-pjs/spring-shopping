package shopping.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.member.domain.Member;
import shopping.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {
    private final MemberRepository memberRepository;

    public boolean existsMember(String email) {
        return memberRepository.existsByEmail(email);
    }

    public Member getMember(String email) {
        return memberRepository.findByEmail(email)
                               .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
    }
}
