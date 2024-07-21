package shopping.wishlist.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shopping.member.application.MemberService;
import shopping.member.application.TestMemberRepository;
import shopping.member.domain.MemberCreate;
import shopping.product.application.ProductReader;
import shopping.product.application.ProductService;
import shopping.product.application.ProductWriter;
import shopping.product.application.StubProfanityChecker;
import shopping.product.application.TestProductRepository;
import shopping.product.domain.Product;
import shopping.product.domain.ProductCreate;

class WishProductServiceTest {
    @DisplayName("위시 리스트에 이미 등록된 상품인 경우 예외를 발생시킨다.")
    @Test
    void test() {
        // given
        MemberService memberService = new MemberService(new TestMemberRepository(), new BCryptPasswordEncoder());
        String memberEmail = "email@email.com";
        memberService.register(new MemberCreate(memberEmail, "password"));

        TestProductRepository productRepository = new TestProductRepository();
        productRepository.save(Product.from(new ProductCreate("productName", "imageUrl", 1000)));

        WishProductService wishProductService = new WishProductService(
                memberService,
                new ProductService(new ProductWriter(productRepository), new ProductReader(productRepository), new StubProfanityChecker(false)),
                new TestWishProductRepository()
        );
        wishProductService.add(memberEmail, 1L);

        // when
        // then
        assertThatThrownBy(() -> wishProductService.add(memberEmail, 1L))
                .isInstanceOf(AlreadyRegisteredWishProductException.class);
    }
}