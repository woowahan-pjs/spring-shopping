package shopping.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.member.service.MemberService;
import shopping.product.adapter.in.api.ProductCreateRequest;
import shopping.product.adapter.in.api.ProductResponse;
import shopping.product.adapter.in.api.ProductUpdateRequest;
import shopping.product.domain.Product;
import shopping.product.domain.ProductImageUrl;
import shopping.product.domain.ProductName;
import shopping.product.domain.ProductPrice;
import shopping.product.domain.ProductRepository;
import shopping.product.domain.ProductStatus;

@ExtendWith(MockitoExtension.class)
class ProductServiceBehaviorTest {
    @Mock
    private MemberService memberService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SlangChecker slangChecker;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(memberService, productRepository, slangChecker);
    }

    @Nested
    class Create {
        @Test
        @DisplayName("유효한 상품 입력이면 상품을 만들고 응답으로 돌려준다")
        void success() {
            // given
            ProductCreateRequest request = new ProductCreateRequest(
                    "상품",
                    "  ",
                    new BigDecimal("10000"),
                    "https://example.com/image.png"
            );
            when(slangChecker.containsSlang("상품")).thenReturn(false);
            when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
                Product product = invocation.getArgument(0);
                setField(product, "id", 1L);
                return product;
            });

            // when
            ProductResponse response = productService.create(1L, request);

            // then
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.description()).isNull();
            verify(memberService).requireActiveSeller(1L);
        }

        @Test
        @DisplayName("상품 이름에 비속어가 있으면 만들지 않는다")
        void throwWhenNameContainsSlang() {
            // given
            ProductCreateRequest request = new ProductCreateRequest(
                    "상품",
                    "설명",
                    new BigDecimal("10000"),
                    "https://example.com/image.png"
            );
            when(slangChecker.containsSlang("상품")).thenReturn(true);

            // when
            // then
            assertThatThrownBy(() -> productService.create(1L, request))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.PRODUCT_NAME_CONTAINS_SLANG);
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefghijklmnop", "가가가가가가가가가가가가가가가가"})
        @DisplayName("상품 이름이 15자를 넘으면 만들지 않는다")
        void throwWhenNameIsTooLong(String name) {
            // given
            ProductCreateRequest request = new ProductCreateRequest(
                    name,
                    "설명",
                    new BigDecimal("10000"),
                    "https://example.com/image.png"
            );

            // when
            // then
            assertThatThrownBy(() -> productService.create(1L, request))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.PRODUCT_NAME_TOO_LONG);
        }

        @ParameterizedTest
        @ValueSource(strings = {"상품!", "name*", "상품@"})
        @DisplayName("허용하지 않은 특수문자가 있으면 만들지 않는다")
        void throwWhenNameContainsDisallowedSpecialCharacters(String name) {
            // given
            ProductCreateRequest request = new ProductCreateRequest(
                    name,
                    "설명",
                    new BigDecimal("10000"),
                    "https://example.com/image.png"
            );

            // when
            // then
            assertThatThrownBy(() -> productService.create(1L, request))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.PRODUCT_NAME_DISALLOWED_SPECIAL_CHARACTERS);
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "-1"})
        @DisplayName("가격이 0 이하이면 만들지 않는다")
        void throwWhenPriceIsInvalid(String price) {
            // given
            ProductCreateRequest request = new ProductCreateRequest(
                    "상품",
                    "설명",
                    new BigDecimal(price),
                    "https://example.com/image.png"
            );

            // when
            // then
            assertThatThrownBy(() -> productService.create(1L, request))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.PRODUCT_PRICE_INVALID);
        }

        @Test
        @DisplayName("이미지 URL 형식이 잘못되면 만들지 않는다")
        void throwWhenImageUrlFormatIsInvalid() {
            // given
            ProductCreateRequest request = new ProductCreateRequest(
                    "상품",
                    "설명",
                    new BigDecimal("10000"),
                    "ht^tp://bad-url"
            );

            // when
            // then
            assertThatThrownBy(() -> productService.create(1L, request))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.PRODUCT_IMAGE_URL_INVALID_FORMAT);
        }

        @Test
        @DisplayName("이미지 URL이 절대 경로가 아니면 만들지 않는다")
        void throwWhenImageUrlIsNotAbsolute() {
            // given
            ProductCreateRequest request = new ProductCreateRequest(
                    "상품",
                    "설명",
                    new BigDecimal("10000"),
                    "/images/item.png"
            );

            // when
            // then
            assertThatThrownBy(() -> productService.create(1L, request))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.PRODUCT_IMAGE_URL_NOT_ABSOLUTE);
        }
    }

    @Test
    @DisplayName("상품이 없으면 조회하지 못한다")
    void getThrowWhenProductDoesNotExist() {
        // given
        when(productRepository.findByIdAndStatus(1L, ProductStatus.ACTIVE)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> productService.get(1L))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
    }

    @Test
    @DisplayName("활성 상품이 있으면 조회 응답을 돌려준다")
    void getReturnProductResponseWhenProductExists() {
        // given
        Product product = createProduct(1L, 10L);
        when(productRepository.findByIdAndStatus(1L, ProductStatus.ACTIVE)).thenReturn(Optional.of(product));

        // when
        ProductResponse response = productService.get(1L);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("상품");
    }

    @Test
    @DisplayName("활성 상품 목록을 응답으로 변환한다")
    void listReturnMappedProducts() {
        // given
        Product product = createProduct(1L, 10L);
        when(productRepository.findByStatusOrderByIdAsc(ProductStatus.ACTIVE)).thenReturn(List.of(product));

        // when
        List<ProductResponse> responses = productService.list();

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("상품 주인만 상품을 수정할 수 있다")
    void updateThrowWhenMemberIsNotOwner() {
        // given
        Product product = createProduct(1L, 2L);
        when(productRepository.findByIdAndStatus(1L, ProductStatus.ACTIVE)).thenReturn(Optional.of(product));

        // when
        // then
        assertThatThrownBy(() -> productService.update(
                1L,
                1L,
                new ProductUpdateRequest(
                        "상품",
                        "설명",
                        new BigDecimal("10000"),
                        "https://example.com/image.png"
                )
        ))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_OWNER_FORBIDDEN);
    }

    @Test
    @DisplayName("상품 주인이면 상품을 수정한다")
    void updateSuccess() {
        // given
        Product product = createProduct(1L, 1L);
        ProductUpdateRequest request = new ProductUpdateRequest(
                "새상품",
                "새설명",
                new BigDecimal("20000"),
                "https://example.com/new-image.png"
        );
        when(productRepository.findByIdAndStatus(1L, ProductStatus.ACTIVE)).thenReturn(Optional.of(product));
        when(slangChecker.containsSlang("새상품")).thenReturn(false);

        // when
        ProductResponse response = productService.update(1L, 1L, request);

        // then
        assertThat(response.name()).isEqualTo("새상품");
        assertThat(response.description()).isEqualTo("새설명");
        verify(memberService).requireActiveSeller(1L);
    }

    @Test
    @DisplayName("상품 주인이면 상품을 삭제한다")
    void deleteSuccess() {
        // given
        Product product = createProduct(1L, 1L);
        when(productRepository.findByIdAndStatus(1L, ProductStatus.ACTIVE)).thenReturn(Optional.of(product));

        // when
        // then
        assertThatCode(() -> productService.delete(1L, 1L)).doesNotThrowAnyException();

        assertThat(product.getStatus()).isEqualTo(ProductStatus.DELETED);
        verify(memberService).requireActiveSeller(1L);
    }

    @Test
    @DisplayName("상품 주인이 아니면 삭제하지 못한다")
    void deleteThrowWhenMemberIsNotOwner() {
        // given
        Product product = createProduct(1L, 2L);
        when(productRepository.findByIdAndStatus(1L, ProductStatus.ACTIVE)).thenReturn(Optional.of(product));

        // when
        // then
        assertThatThrownBy(() -> productService.delete(1L, 1L))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_OWNER_FORBIDDEN);
    }

    private Product createProduct(Long productId, Long memberId) {
        Product product = Product.create(
                new ProductName("상품"),
                "설명",
                new ProductImageUrl("https://example.com/image.png"),
                new ProductPrice(new BigDecimal("10000")),
                memberId
        );
        setField(product, "id", productId);
        return product;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
