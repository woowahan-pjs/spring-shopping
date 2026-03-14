package shopping.wish.service;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import shopping.member.domain.MemberRepository;
import shopping.wish.domain.RemoveWishByProduct;

@Transactional
public class RemoveWishByProductService implements RemoveWishByProduct {

    private final MemberRepository memberRepository;

    public RemoveWishByProductService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void execute(UUID productId) {
        memberRepository.removeWishByProductId(productId);
    }
}
