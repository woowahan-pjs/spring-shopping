package shopping.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shopping.component.MemberContext;
import shopping.component.ProductNameValidator;
import shopping.controller.dto.Wish.GetWishResponseDto;
import shopping.controller.dto.product.AddProductRequestDto;
import shopping.controller.dto.product.GetProductResponseDto;
import shopping.repository.ProductRepository;
import shopping.repository.WishRepository;

@SpringBootTest
class WishServiceIntegrationTest {

	@Autowired
	private WishService wishService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private WishRepository wishRepository;

	@Mock
	private ProductNameValidator productNameValidator;

	private static final Long MEMBER_ID = 1L;

	@BeforeEach
	void setUp() {
		MemberContext.setMemberId(MEMBER_ID);
		doNothing().when(productNameValidator).validate(anyString());
	}

	@AfterEach
	void tearDown() {
		wishRepository.deleteAll();
		productRepository.deleteAll();
		MemberContext.clear();
	}

	@Test
	void addWish_위시리스트_추가_성공() {
		productService.addProduct(new AddProductRequestDto("ProductA", 10000, "http://image.url"));
		Long productId = productService.findAllProducts().getFirst().getId();

		wishService.addWish(productId);

		List<GetWishResponseDto> wishes = wishService.findAll();
		assertThat(wishes).hasSize(1);
		assertThat(wishes.getFirst().getProductId()).isEqualTo(productId);
		assertThat(wishes.getFirst().getProductName()).isEqualTo("ProductA");
	}

	@Test
	void findAll_회원의_위시리스트만_조회() {
		productService.addProduct(new AddProductRequestDto("ProductA", 10000, "http://a.url"));
		productService.addProduct(new AddProductRequestDto("ProductB", 20000, "http://b.url"));
		List<Long> productIds = productService.findAllProducts().stream()
			.map(GetProductResponseDto::getId)
			.toList();
		wishService.addWish(productIds.get(0));
		wishService.addWish(productIds.get(1));

		// 다른 회원의 위시는 조회되지 않음
		MemberContext.setMemberId(2L);
		wishService.addWish(productIds.get(0));

		MemberContext.setMemberId(MEMBER_ID);
		List<GetWishResponseDto> wishes = wishService.findAll();
		assertThat(wishes).hasSize(2);
	}

	@Test
	void delete_위시리스트_삭제_성공() {
		productService.addProduct(new AddProductRequestDto("ProductA", 10000, "http://image.url"));
		Long productId = productService.findAllProducts().getFirst().getId();
		wishService.addWish(productId);
		Long wishId = wishRepository.findAllByMemberId(MEMBER_ID).getFirst().getId();

		wishService.delete(wishId);

		assertThat(wishService.findAll()).isEmpty();
	}
}