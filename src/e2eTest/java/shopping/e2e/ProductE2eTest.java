package shopping.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import com.fasterxml.jackson.core.type.TypeReference;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import shopping.common.ErrorResponse;
import shopping.e2e.support.AbstractE2eTest;
import shopping.e2e.support.AuthSession;
import shopping.e2e.support.TestAccount;
import shopping.product.adapter.in.api.ProductCreateRequest;
import shopping.product.adapter.in.api.ProductResponse;
import shopping.product.adapter.in.api.ProductUpdateRequest;
import shopping.product.domain.Product;

@DisplayName("[상품] 상품 통합 테스트")
class ProductE2eTest extends AbstractE2eTest {
    @Nested
    @DisplayName("조회")
    class Read {
        @Test
        @DisplayName("비회원은 상품 목록을 조회할 수 있다")
        void guestCanReadProductList() {
            TestAccount seller = seedSeller();
            seedProduct(seller.memberId(), "Notebook");

            ResponseEntity<String> response = get("/api/products", jsonHeaders());

            assertStatus(response, HttpStatus.OK);
            List<ProductResponse> products = readBody(response, new TypeReference<>() {
            });
            assertThat(products).hasSize(1);
        }

        @Test
        @DisplayName("비회원은 상품 상세를 조회할 수 있다")
        void guestCanReadProductDetail() {
            TestAccount seller = seedSeller();
            Product product = seedProduct(seller.memberId(), "Notebook");

            ResponseEntity<String> response = get("/api/products/" + product.getId(), jsonHeaders());

            assertStatus(response, HttpStatus.OK);
            ProductResponse productResponse = readBody(response, ProductResponse.class);
            assertThat(productResponse.id()).isEqualTo(product.getId());
        }
    }

    @Nested
    @DisplayName("생성")
    class Create {
        @Test
        @DisplayName("판매자는 자기 상품을 만들 수 있다")
        void sellerCanCreateProduct() {
            TestAccount seller = seedSeller();
            AuthSession sellerSession = login(seller.email(), seller.password());
            ProductCreateRequest request = new ProductCreateRequest(
                    "가가가가가가가가가가가가가가가",
                    "상품 설명",
                    new BigDecimal("120000"),
                    "https://example.com/image.png"
            );

            ResponseEntity<String> response = post("/api/products", request, bearerHeaders(sellerSession.accessToken()));

            assertStatus(response, HttpStatus.CREATED);
            ProductResponse productResponse = readBody(response, ProductResponse.class);
            assertThat(productResponse.name()).isEqualTo("가가가가가가가가가가가가가가가");
        }

        @Test
        @DisplayName("일반 회원은 상품을 만들 수 없다")
        void userCannotCreateProduct() {
            AuthSession userSession = registerMember(uniqueEmail("user"), "password123!");
            ProductCreateRequest request = new ProductCreateRequest(
                    "Notebook",
                    "노트북 설명",
                    new BigDecimal("120000"),
                    "https://example.com/image.png"
            );

            ResponseEntity<String> response = post("/api/products", request, bearerHeaders(userSession.accessToken()));

            assertStatus(response, HttpStatus.FORBIDDEN);
            ErrorResponse errorResponse = readError(response);
            assertThat(errorResponse.code()).isEqualTo("MEMBER_SELLER_REQUIRED");
        }

        @ParameterizedTest(name = "[{index}] 상품명={0}")
        @ValueSource(strings = {
                "abcdefghijklmnop",
                "가가가가가가가가가가가가가가가가"
        })
        @DisplayName("이름이 16자면 상품을 만들지 않는다")
        void createRejectWhenNameExceedsLimit(String name) {
            TestAccount seller = seedSeller();
            AuthSession sellerSession = login(seller.email(), seller.password());
            ProductCreateRequest request = new ProductCreateRequest(
                    name,
                    "상품 설명",
                    new BigDecimal("10000"),
                    "https://example.com/image.png"
            );

            ResponseEntity<String> response = post("/api/products", request, bearerHeaders(sellerSession.accessToken()));

            assertStatus(response, HttpStatus.BAD_REQUEST);
            ErrorResponse errorResponse = readError(response);
            assertThat(errorResponse.code()).isEqualTo("PRODUCT_NAME_TOO_LONG");
        }

        @ParameterizedTest(name = "[{index}] 가격={0}")
        @ValueSource(strings = {
                "0",
                "-1"
        })
        @DisplayName("유효하지 않은 가격이면 상품을 만들지 않는다")
        void createRejectWhenPriceIsInvalid(String price) {
            TestAccount seller = seedSeller();
            AuthSession sellerSession = login(seller.email(), seller.password());
            ProductCreateRequest request = new ProductCreateRequest(
                    "Notebook",
                    "상품 설명",
                    new BigDecimal(price),
                    "https://example.com/image.png"
            );

            ResponseEntity<String> response = post("/api/products", request, bearerHeaders(sellerSession.accessToken()));

            assertStatus(response, HttpStatus.BAD_REQUEST);
            ErrorResponse errorResponse = readError(response);
            assertThat(errorResponse.code()).isEqualTo("INVALID_INPUT");
        }

        @Test
        @DisplayName("절대 URL이 아니면 상품을 만들지 않는다")
        void createRejectWhenImageUrlIsNotAbsolute() {
            TestAccount seller = seedSeller();
            AuthSession sellerSession = login(seller.email(), seller.password());
            ProductCreateRequest request = new ProductCreateRequest(
                    "Notebook",
                    "상품 설명",
                    new BigDecimal("10000"),
                    "/image.png"
            );

            ResponseEntity<String> response = post("/api/products", request, bearerHeaders(sellerSession.accessToken()));

            assertStatus(response, HttpStatus.BAD_REQUEST);
            ErrorResponse errorResponse = readError(response);
            assertThat(errorResponse.code()).isEqualTo("PRODUCT_IMAGE_URL_NOT_ABSOLUTE");
        }

        @Test
        @DisplayName("허용하지 않은 특수문자가 있으면 상품을 만들지 않는다")
        void createRejectWhenNameContainsDisallowedCharacter() {
            TestAccount seller = seedSeller();
            AuthSession sellerSession = login(seller.email(), seller.password());
            ProductCreateRequest request = new ProductCreateRequest(
                    "Notebook!",
                    "상품 설명",
                    new BigDecimal("10000"),
                    "https://example.com/image.png"
            );

            ResponseEntity<String> response = post("/api/products", request, bearerHeaders(sellerSession.accessToken()));

            assertStatus(response, HttpStatus.BAD_REQUEST);
            ErrorResponse errorResponse = readError(response);
            assertThat(errorResponse.code()).isEqualTo("PRODUCT_NAME_DISALLOWED_SPECIAL_CHARACTERS");
        }

        @Test
        @DisplayName("영문 비속어가 있으면 상품을 만들지 않는다")
        void createRejectWhenEnglishSlangExists() {
            TestAccount seller = seedSeller();
            AuthSession sellerSession = login(seller.email(), seller.password());
            ProductCreateRequest request = new ProductCreateRequest(
                    "fuck",
                    "상품 설명",
                    new BigDecimal("10000"),
                    "https://example.com/image.png"
            );

            ResponseEntity<String> response = post("/api/products", request, bearerHeaders(sellerSession.accessToken()));
            abortWhenSlangApiIsUnavailable(response);

            assertStatus(response, HttpStatus.BAD_REQUEST);
            ErrorResponse errorResponse = readError(response);
            assertThat(errorResponse.code()).isEqualTo("PRODUCT_NAME_CONTAINS_SLANG");
        }

        @Test
        @DisplayName("한국어 비속어가 있으면 상품을 만들지 않는다")
        void createRejectWhenKoreanSlangExists() {
            TestAccount seller = seedSeller();
            AuthSession sellerSession = login(seller.email(), seller.password());
            ProductCreateRequest request = new ProductCreateRequest(
                    "시발",
                    "상품 설명",
                    new BigDecimal("10000"),
                    "https://example.com/image.png"
            );

            ResponseEntity<String> response = post("/api/products", request, bearerHeaders(sellerSession.accessToken()));

            assertStatus(response, HttpStatus.BAD_REQUEST);
            ErrorResponse errorResponse = readError(response);
            assertThat(errorResponse.code()).isEqualTo("PRODUCT_NAME_CONTAINS_SLANG");
        }
    }

    @Nested
    @DisplayName("수정 및 삭제")
    class UpdateAndDelete {
        @Test
        @DisplayName("판매자는 자기 상품을 수정할 수 있다")
        void sellerCanUpdateOwnProduct() {
            TestAccount seller = seedSeller();
            AuthSession sellerSession = login(seller.email(), seller.password());
            Product product = seedProduct(seller.memberId(), "Notebook");
            ProductUpdateRequest request = new ProductUpdateRequest(
                    "새상품",
                    "수정 설명",
                    new BigDecimal("150000"),
                    "https://example.com/new-image.png"
            );

            ResponseEntity<String> response = put(
                    "/api/products/" + product.getId(),
                    request,
                    bearerHeaders(sellerSession.accessToken())
            );

            assertStatus(response, HttpStatus.OK);
            ProductResponse productResponse = readBody(response, ProductResponse.class);
            assertThat(productResponse.name()).isEqualTo("새상품");
        }

        @Test
        @DisplayName("다른 판매자는 남의 상품을 수정할 수 없다")
        void otherSellerCannotUpdateForeignProduct() {
            TestAccount owner = seedSeller();
            Product product = seedProduct(owner.memberId(), "Notebook");
            TestAccount otherSeller = seedSeller();
            AuthSession otherSellerSession = login(otherSeller.email(), otherSeller.password());
            ProductUpdateRequest request = new ProductUpdateRequest(
                    "다른상품",
                    "수정 설명",
                    new BigDecimal("150000"),
                    "https://example.com/new-image.png"
            );

            ResponseEntity<String> response = put(
                    "/api/products/" + product.getId(),
                    request,
                    bearerHeaders(otherSellerSession.accessToken())
            );

            assertStatus(response, HttpStatus.FORBIDDEN);
            ErrorResponse errorResponse = readError(response);
            assertThat(errorResponse.code()).isEqualTo("PRODUCT_OWNER_FORBIDDEN");
        }

        @Test
        @DisplayName("판매자는 자기 상품을 삭제할 수 있다")
        void sellerCanDeleteOwnProduct() {
            TestAccount seller = seedSeller();
            AuthSession sellerSession = login(seller.email(), seller.password());
            Product product = seedProduct(seller.memberId(), "Notebook");

            ResponseEntity<String> response = delete(
                    "/api/products/" + product.getId(),
                    bearerHeaders(sellerSession.accessToken())
            );

            assertStatus(response, HttpStatus.NO_CONTENT);
            ResponseEntity<String> getResponse = get("/api/products/" + product.getId(), jsonHeaders());
            assertStatus(getResponse, HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("다른 판매자는 남의 상품을 삭제할 수 없다")
        void otherSellerCannotDeleteForeignProduct() {
            TestAccount owner = seedSeller();
            Product product = seedProduct(owner.memberId(), "Notebook");
            TestAccount otherSeller = seedSeller();
            AuthSession otherSellerSession = login(otherSeller.email(), otherSeller.password());

            ResponseEntity<String> response = delete(
                    "/api/products/" + product.getId(),
                    bearerHeaders(otherSellerSession.accessToken())
            );

            assertStatus(response, HttpStatus.FORBIDDEN);
            ErrorResponse errorResponse = readError(response);
            assertThat(errorResponse.code()).isEqualTo("PRODUCT_OWNER_FORBIDDEN");
        }
    }

    private void abortWhenSlangApiIsUnavailable(ResponseEntity<String> response) {
        if (response.getStatusCode() != HttpStatus.SERVICE_UNAVAILABLE) {
            return;
        }

        ErrorResponse errorResponse = readError(response);
        assumeFalse(errorResponse.code().startsWith("SLANG_"), "PurgoMalum is unavailable");
    }
}
