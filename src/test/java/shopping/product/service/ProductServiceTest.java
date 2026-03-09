package shopping.product.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.member.service.MemberService;
import shopping.product.adapter.in.api.ProductCreateRequest;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("[상품] 상품 서비스 비속어 검증 단위 테스트")
class ProductServiceTest {
    @Mock
    private MemberService memberService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SlangChecker slangChecker;

    @Test
    @DisplayName("상품 이름에 비속어가 있으면 상품을 만들지 않는다")
    void createThrowWhenProductNameContainsSlang() {
        // given
        ProductService productService = new ProductService(memberService, productRepository, slangChecker);
        ProductCreateRequest request = new ProductCreateRequest(
                "씨발 쿠션",
                "설명",
                new BigDecimal("1000"),
                "https://example.com/image.png"
        );
        when(slangChecker.containsSlang("씨발 쿠션")).thenReturn(true);

        // when
        // then
        assertThatThrownBy(() -> productService.create(1L, request))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_NAME_CONTAINS_SLANG);

        verify(memberService).requireActiveSeller(1L);
        verify(productRepository, never()).save(any(Product.class));
    }
}
