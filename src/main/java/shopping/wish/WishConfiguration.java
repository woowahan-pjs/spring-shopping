package shopping.wish;

import shopping.wish.domain.*;
import shopping.wish.service.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import shopping.member.domain.MemberRepository;

@Configuration
public class WishConfiguration {

    @Bean
    public AddWish addWish(MemberRepository memberRepository) {
        return new AddWishService(memberRepository);
    }

    @Bean
    public RemoveWish removeWish(MemberRepository memberRepository) {
        return new RemoveWishService(memberRepository);
    }

    @Bean
    public FindWish findWish(MemberRepository memberRepository) {
        return new FindWishService(memberRepository);
    }
}
