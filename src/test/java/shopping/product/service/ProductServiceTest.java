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
import shopping.product.dto.ProductUpdateRequest;
import shopping.product.repository.ProductRepository;
import shopping.product.validator.ProductNameValidator;

import java.util.List;
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

        //given
        Product product = Product.create("아이폰", 1_000_000);

        ReflectionTestUtils.setField(product, "id", 1L);

        given(productRepository.findById(1L))
                .willReturn(Optional.of(product));

        //when
        ProductResponse response = productService.getProduct(1L);

        //then
        assertThat(response.getName()).isEqualTo("아이폰");
        assertThat(response.getPrice()).isEqualTo(1_000_000);
    }

    @Test
    void 상품을_수정한다() {

        //given
        Product product = Product.create("아이폰", 1_000_000);

        given(productRepository.findById(1L))
                .willReturn(Optional.of(product));

        ProductUpdateRequest request = new ProductUpdateRequest();

        ReflectionTestUtils.setField(request, "name", "아이폰16");
        ReflectionTestUtils.setField(request, "price", 2_000_000);
        ReflectionTestUtils.setField(request, "imageUrl", "new_iphone.jpg");

        //when
        productService.updateProduct(1L, request);

        //then
        assertThat(product.getName()).isEqualTo("아이폰16");
        assertThat(product.getPrice()).isEqualTo(2_000_000);

        verify(productNameValidator).validate("아이폰16");
    }

    @Test
    void 상품을_삭제한다() {

        //given
        Product product  = Product.create("아이폰", 1_000_000);

        given(productRepository.findById(1L))
                .willReturn(Optional.of(product));

        //when
        productService.deleteProduct(1L);

        //then
        verify(productRepository).delete(product);
    }

    @Test
    void 상품_목록을_조회한다() {

        //given
        Product product1 = Product.create("아이폰", 1_000_000);
        Product product2 = Product.create("맥북", 2_000_000);

        given(productRepository.findAll())
                .willReturn(List.of(product1, product2));

        //when
        List<ProductResponse> result = productService.getProducts();

        //then
        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getName()).isEqualTo("아이폰");

        verify(productRepository).findAll();
    }
}
