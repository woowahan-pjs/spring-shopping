package shopping.member.client.applicaton;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import shopping.fake.FakeProductRepository;
import shopping.fake.FakeProfanityChecker;
import shopping.fixture.ClientFixture;
import shopping.fixture.ProductFixture;
import shopping.member.client.applicaton.dto.WishProductResponse;
import shopping.member.client.domain.Client;
import shopping.member.client.exception.DuplicateWishProductException;
import shopping.member.client.exception.NotFoundWishProductException;
import shopping.product.application.ProductService;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;
import shopping.product.exception.NotFoundProductException;

@DisplayName("ClientWishService")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ClientWishServiceTest {

    private final ProductRepository productRepository = new FakeProductRepository();
    private ClientWishService clientWishService;

    @BeforeEach
    void setUp() {
        final ProductService productService = new ProductService(
                new FakeProfanityChecker(),
                productRepository);
        final WishProductMapper wishProductMapper = new WishProductMapper(productService);
        clientWishService = new ClientWishService(wishProductMapper,
                (event) -> System.out.println("이벤트 발행"));
    }

    @Test
    void 존재하지_않는_Product의_하트를_누르면_예외를_던진다() {
        assertThatThrownBy(() -> clientWishService.wish(1L, createClient()))
                .isInstanceOf(NotFoundProductException.class);
    }

    @Test
    void Product의_하트를_누르면_Client의_위시상품리스트에_추가된다() {
        final Client client = createClient();
        final Product product = createProduct();

        clientWishService.wish(product.getId(), client);

        assertThat(client.productIds()).hasSize(1);
    }

    @Test
    void 중복되는_Product의_하트를_누르면_예외를_던진다() {
        final Client client = createClient();
        final Product product = createProduct();
        clientWishService.wish(product.getId(), client);

        assertThatThrownBy(() -> clientWishService.wish(product.getId(), client))
                .isInstanceOf(DuplicateWishProductException.class);
    }

    @Test
    void Product의_하트를_취소하면_Client의_위시상품리스트에서_삭제된다() {
        final Client client = createClient();
        final Product product = createProduct();
        clientWishService.wish(product.getId(), client);

        clientWishService.unWish(product.getId(), client);

        assertThat(client.productIds()).hasSize(0);
    }

    @Test
    void 위시상품에_없는_상품의_하트를_취소하면_예외를_던진다() {
        final Client client = createClient();

        assertThatThrownBy(() -> clientWishService.unWish(1L, client))
                .isInstanceOf(NotFoundWishProductException.class);
    }

    @Test
    void 위시상품리스트에_추가된_상품들을_조회할_수_있다() {
        final Client client = createClient();
        final Product product = createProduct();
        clientWishService.wish(product.getId(), client);

        final List<WishProductResponse> responses = clientWishService.findAll(client);

        assertThat(responses).hasSize(1);
    }

    @Test
    void 위시상품조회_시_추가된_상품이_없다면_빈리스트를_반환한다() {
        final Client client = createClient();

        final List<WishProductResponse> responses = clientWishService.findAll(client);

        assertThat(responses).hasSize(0);
    }

    private Client createClient() {
        return ClientFixture.createClient();
    }

    private Product createProduct() {
        final Product product = ProductFixture.createProduct();
        return productRepository.save(product);
    }
}