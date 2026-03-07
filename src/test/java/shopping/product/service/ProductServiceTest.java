package shopping.product.service;

import org.junit.jupiter.api.Test;
import shopping.product.domain.Product;
import shopping.product.dto.ProductCreateRequest;
import shopping.product.repository.ProductRepository;
import shopping.product.validator.ProductNameValidator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

public class ProductServiceTest {

    @Test
    void 상품을_생성한다() {

        //given
        ProductRepository productRepository = mock(ProductRepository.class);
        ProductNameValidator productNameValidator = mock(ProductNameValidator.class);

        ProductService productService = new ProductService(productNameValidator, productRepository);

        ProductCreateRequest request = new ProductCreateRequest("아이폰", 1_000_000);

        //when
        productService.createProduct(request);

        //then
        verify(productNameValidator).validate("아이폰");
        verify(productRepository).save(any(Product.class));
    }
}
