package shopping.wish;

import java.util.NoSuchElementException;
import java.util.UUID;

import shopping.member.Member;
import shopping.member.MemberRepository;

public class AddWishService implements AddWish {

    private final MemberRepository memberRepository;

    public AddWishService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Wish execute(UUID memberId, UUID productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        Wish wish = member.wish(productId);
        memberRepository.save(member);
        return wish;
    }
}
