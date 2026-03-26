package shopping.wish.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.member.domain.Member;
import shopping.member.service.MemberQueryService;
import shopping.product.domain.Product;
import shopping.product.service.ProductQueryService;
import shopping.wish.domain.Wish;
import shopping.wish.repository.WishRepository;
import shopping.wish.service.dto.WishAddInput;
import shopping.wish.service.dto.WishAddOutput;

@Service
@RequiredArgsConstructor
@Transactional
public class WishCommandService {
    private final WishRepository wishRepository;
    private final MemberQueryService memberQueryService;
    private final ProductQueryService productQueryService;

    public WishAddOutput add(WishAddInput input) {
        Member member = memberQueryService.getMember(input.memberId());
        Product product = productQueryService.findProduct(input.productId())
                                             .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        wishRepository.findByMemberIdAndProductIdAndDeletedFalse(member.getId(), product.getId())
                      .ifPresent(w -> { throw new IllegalArgumentException("이미 위시리스트에 있는 상품입니다."); });
        Wish wish = wishRepository.save(Wish.create(member, product));
        return WishAddOutput.of(wish, product);
    }

    public void delete(Long wishId, Long memberId) {
        Wish wish = wishRepository.findByIdAndMemberIdAndDeletedFalse(wishId, memberId)
                                  .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 위시리스트입니다."));
        wish.delete();
    }
}
