package shopping.wishlist.application;

import org.springframework.stereotype.Service;
import shopping.member.application.MemberService;
import shopping.member.domain.Member;
import shopping.product.application.ProductService;
import shopping.product.domain.Product;
import shopping.wishlist.domain.WishProduct;

@Service
public class WishProductService {
    private final MemberService memberService;
    private final ProductService productService;
    private final WishProductRepository wishProductRepository;

    public WishProductService(MemberService memberService, ProductService productService, WishProductRepository wishProductRepository) {
        this.memberService = memberService;
        this.productService = productService;
        this.wishProductRepository = wishProductRepository;
    }

    public void add(String email, Long productId) {
        Member member = memberService.getByEmail(email);
        Product product = productService.getById(productId);

        WishProduct wishProduct = WishProduct.of(member, product);
        wishProductRepository.save(wishProduct);
    }
}
