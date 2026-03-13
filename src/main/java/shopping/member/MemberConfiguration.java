package shopping.member;

import shopping.member.domain.*;
import shopping.member.service.*;

import io.jsonwebtoken.Jwts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberConfiguration {

    @Bean
    public TokenProvider tokenProvider() {
        return new JwtTokenProvider(Jwts.SIG.HS256.key().build(), 3600000L);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BcryptPasswordEncoder();
    }

    @Bean
    public RegisterMember registerMember(MemberRepository memberRepository,
            PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        return new RegisterMemberService(memberRepository, passwordEncoder, tokenProvider);
    }

    @Bean
    public LoginMember loginMember(MemberRepository memberRepository, TokenProvider tokenProvider,
            PasswordEncoder passwordEncoder) {
        return new LoginMemberService(memberRepository, tokenProvider, passwordEncoder);
    }
}
