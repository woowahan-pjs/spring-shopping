package shopping.member;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberConfiguration {

    @Bean
    public RegisterMember registerMember(MemberRepository memberRepository) {
        return new RegisterMemberService(memberRepository);
    }

    @Bean
    public LoginMember loginMember(MemberRepository memberRepository) {
        return new LoginMemberService(memberRepository);
    }
}
