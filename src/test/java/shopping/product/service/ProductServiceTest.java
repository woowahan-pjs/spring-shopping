package shopping.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import shopping.product.domain.Product;
import shopping.product.dto.ProductCreateRequest;
import shopping.product.dto.ProductResponse;
import shopping.product.repository.ProductRepository;
import shopping.product.validator.ProductNameValidator;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductNameValidator productNameValidator;

    @InjectMocks
    private ProductService productService;

    @Test
    void 상품을_생성한다() {

        //given
        ProductCreateRequest request = new ProductCreateRequest("아이폰", 1_000_000);

        //when
        productService.createProduct(request);

        //then
        verify(productNameValidator).validate("아이폰");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void 특정_상품을_조회한다() {

        Product product = Product.create("아이폰", 1_000_000);

        ReflectionTestUtils.setField(product, "id", 1L);

        given(productRepository.findById(1L))
                .willReturn(Optional.of(product));

        ProductResponse response = productService.getProduct(1L);

        assertThat(response.getName()).isEqualTo("아이폰");
        assertThat(response.getPrice()).isEqualTo(1_000_000);
    }
}
