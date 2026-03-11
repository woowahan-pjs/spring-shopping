package shopping.wish.service;

import shopping.wish.domain.*;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;

@Transactional
public class AddWishService implements AddWish {

    private final MemberRepository memberRepository;

    public AddWishService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Wish execute(UUID memberId, UUID productId, long wishedPrice) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        Wish wish = member.wish(productId, wishedPrice);
        memberRepository.save(member);
        return wish;
    }
}
