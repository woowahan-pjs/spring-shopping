package shopping.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import shopping.controller.dto.product.AddProductRequestDto;
import shopping.controller.dto.product.GetProductResponseDto;
import shopping.controller.dto.product.UpdateProductRequestDto;
import shopping.exception.NotFoundException;
import shopping.repository.ProductRepository;

@SpringBootTest
class ProductServiceIntegrationTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Mock
	private ProductNameValidator productNameValidator;

	@BeforeEach
	void setUp() {
		MemberContext.setMemberId(1L);
		doNothing().when(productNameValidator).validate(anyString());
	}

	@AfterEach
	void tearDown() {
		productRepository.deleteAll();
		MemberContext.clear();
	}

	@Test
	void addProduct_상품_저장후_조회_성공() {
		AddProductRequestDto request = new AddProductRequestDto("CreateProduct", 10000, "http://image.url");

		productService.addProduct(request);

		List<GetProductResponseDto> products = productService.findAllProducts();
		assertThat(products).hasSize(1);
		assertThat(products.get(0).getName()).isEqualTo("CreateProduct");
		assertThat(products.get(0).getPrice()).isEqualTo(10000);
	}

	@Test
	void findProduct_존재하지_않는_상품_예외발생() {
		assertThatThrownBy(() -> productService.findProduct(999L))
			.isInstanceOf(NotFoundException.class);
	}

	@Test
	void updateProduct_상품_수정_성공() {
		AddProductRequestDto addRequest = new AddProductRequestDto("CreateProduct", 5000, "http://old.url");
		productService.addProduct(addRequest);
		Long productId = productService.findAllProducts().get(0).getId();

		UpdateProductRequestDto updateRequest = new UpdateProductRequestDto(productId, "UpdateProduct", 9999, "http://new.url");
		productService.updateProduct(updateRequest);

		GetProductResponseDto updated = productService.findProduct(productId);
		assertThat(updated.getName()).isEqualTo("UpdateProduct");
		assertThat(updated.getPrice()).isEqualTo(9999);
	}

	@Test
	void deleteProduct_상품_삭제_성공() {
		AddProductRequestDto request = new AddProductRequestDto("DeleteProduct", 3000, "http://del.url");
		productService.addProduct(request);
		Long productId = productService.findAllProducts().get(0).getId();

		productService.deleteProduct(productId);

		assertThatThrownBy(() -> productService.findProduct(productId))
			.isInstanceOf(NotFoundException.class);
	}
}
