package shopping.wish;

import java.util.NoSuchElementException;

import shopping.member.Member;
import shopping.member.MemberRepository;

public class RemoveWishService implements RemoveWish {

    private final MemberRepository memberRepository;

    public RemoveWishService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void execute(Long memberId, Long productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        member.removeWish(productId);
        memberRepository.save(member);
    }
}
