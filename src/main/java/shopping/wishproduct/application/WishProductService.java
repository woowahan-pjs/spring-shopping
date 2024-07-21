package shopping.wishproduct.application;

import java.util.List;
import org.springframework.stereotype.Service;
import shopping.member.application.MemberService;
import shopping.member.domain.Member;
import shopping.product.application.ProductService;
import shopping.product.domain.Product;
import shopping.wishproduct.domain.WishProduct;

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
        if (wishProductRepository.existsByMemberAndProduct(member, product)) {
            throw new AlreadyRegisteredWishProductException();
        }

        WishProduct wishProduct = WishProduct.of(member, product);
        wishProductRepository.save(wishProduct);
    }

    public List<WishProduct> getAll(String email) {
        Member member = memberService.getByEmail(email);
        return wishProductRepository.findAllByMember(member);
    }

    public void remove(String email, Long productId) {
        Member member = memberService.getByEmail(email);
        wishProductRepository.remove(member, productId);
    }
}
