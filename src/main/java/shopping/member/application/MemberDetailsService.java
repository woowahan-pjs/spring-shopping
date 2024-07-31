package shopping.member.application;


import org.springframework.stereotype.Service;
import shopping.auth.application.UserDetail;
import shopping.auth.application.UserDetailsService;
import shopping.member.domain.Member;
import shopping.member.exception.MemberNotFoundException;
import shopping.member.repository.MemberRepository;

@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberDetailsService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetail loadUserByEmail(final String email) {
        final Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));
        return new UserDetail(member.getId(), member.getEmail(), member.getPassword());
    }
}
