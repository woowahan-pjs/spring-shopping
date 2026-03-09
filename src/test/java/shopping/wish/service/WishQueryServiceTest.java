package shopping.wish.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;
import shopping.wish.domain.Wish;
import shopping.wish.repository.WishRepository;
import shopping.wish.service.dto.WishOutput;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class WishQueryServiceTest {

    @Autowired
    private WishQueryService wishQueryService;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("회원의 활성 위시리스트 목록을 조회한다")
    void test01() {
        // arrange
        Product product = productRepository.save(
                Product.builder().name("상품명").price(10000L).imageUrl("https://example.com/image.jpg").build()
        );
        Product deletedProduct = productRepository.save(
                Product.builder().name("삭제상품").price(20000L).imageUrl("https://example.com/deleted.jpg").build()
        );
        wishRepository.save(Wish.builder().memberId(1L).productId(product.getId()).build());
        Wish deletedWish = wishRepository.save(Wish.builder().memberId(1L).productId(deletedProduct.getId()).build());
        deletedWish.delete();
        wishRepository.save(Wish.builder().memberId(2L).productId(product.getId()).build());

        // act
        List<WishOutput> result = wishQueryService.findAll(1L);

        // assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().productId()).isEqualTo(product.getId());
        assertThat(result.getFirst().productName()).isEqualTo("상품명");
        assertThat(result.getFirst().price()).isEqualTo(10000L);
        assertThat(result.getFirst().imageUrl()).isEqualTo("https://example.com/image.jpg");
    }

    @Test
    @DisplayName("위시리스트가 없는 회원은 빈 목록을 반환한다")
    void test02() {
        // arrange
        Long memberId = 1L;

        // act
        List<WishOutput> result = wishQueryService.findAll(memberId);

        // assert
        assertThat(result).isEmpty();
    }
}
