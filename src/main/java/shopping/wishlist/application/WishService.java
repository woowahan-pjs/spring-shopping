package shopping.wishlist.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.auth.domain.LoginMember;
import shopping.exception.BadRequestException;
import shopping.exception.NotFoundException;
import shopping.member.application.MemberService;
import shopping.product.application.ProductService;
import shopping.product.domain.Product;
import shopping.wishlist.domain.Wish;
import shopping.wishlist.domain.WishList;
import shopping.wishlist.domain.WishRepository;
import shopping.wishlist.dto.WishRequest;
import shopping.wishlist.dto.WishResponse;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class WishService {
    private WishRepository wishRepository;
    private ProductService productService;
    private MemberService memberService;

    public WishService(ProductService productService, WishRepository wishRepository
            , MemberService memberService) {
        this.productService = productService;
        this.wishRepository = wishRepository;
        this.memberService = memberService;
    }


    @Transactional
    public WishResponse.WishDetail addWishList(LoginMember loginMember, WishRequest.RegWishList request) {
        Product product = productService.findProductByPrdctSn(request.getPrdctSn());
        checkExistWish(loginMember.getId(), product);
        Wish persistWishList = wishRepository.save(request.toWishList(loginMember.getId(), product));

        return WishResponse.WishDetail.from(persistWishList);
    }

    private void checkExistMember(Long mbrSn) {
        boolean isExist = memberService.isExistMemberById(mbrSn);
        if(!isExist) {
            throw new BadRequestException("해당 회원이 존재하지 않습니다.");
        }
    }

    public WishResponse.WishListRes findAllWishList(Long mbrSn) {
        WishList wishList = findWishListByMbrSn(mbrSn);
        return WishResponse.WishListRes.from(wishList);
    }

    @Transactional
    public WishResponse.WishDetail updateWishProductCntById(LoginMember loginMember, Long wishSn, WishRequest.ModWishProductCnt request) {
        Wish wish = findWishByIdAndMbrSn(wishSn, loginMember.getId());
        wish.updateCnt(request.isAdd(), request.getCnt());
        return WishResponse.WishDetail.from(wish);
    }

    @Transactional
    public void deleteWishById(Long id) {
        Wish persistWish = findWishById(id);
        wishRepository.delete(persistWish);
    }


    private WishList findWishListByMbrSn(Long mbrSn) {
        List<Wish> wishList = wishRepository.findAllByMbrSn(mbrSn);
        return new WishList(wishList);
    }

    private Wish findWishById(Long wishSn) {
        return wishRepository.findById(wishSn)
                .orElseThrow(() -> new NotFoundException("해당 위시 리스트가 존재하지 않습니다."));
    }

    private Wish findWishByIdAndMbrSn(Long wishSn, Long mbrSn) {
        return Optional.ofNullable(wishRepository.findByWishSnAndMbrSn(wishSn, mbrSn))
                .orElseThrow(() -> new NotFoundException("로그인 한 회원의 위시리스트 정보가 아닙니다."));
    }

    private void checkExistWish(Long mbrSn, Product product) {
        boolean isExist = wishRepository.existsByMbrSnAndProduct(mbrSn, product);
        if (isExist) {
            throw new BadRequestException("이미 등록된 위시리스트 입니다.");
        }
    }

}
