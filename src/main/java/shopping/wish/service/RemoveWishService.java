package shopping.wish.service;

import shopping.wish.domain.*;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;

@Transactional
public class RemoveWishService implements RemoveWish {

    private final MemberRepository memberRepository;

    public RemoveWishService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void execute(UUID memberId, UUID productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        member.removeWish(productId);
        memberRepository.save(member);
    }
}
