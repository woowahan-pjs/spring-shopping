package shopping.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import shopping.e2e.support.AbstractE2eTest;
import shopping.e2e.support.AuthSession;
import shopping.e2e.support.TestAccount;
import shopping.product.adapter.in.api.ProductCreateRequest;
import shopping.product.adapter.in.api.ProductResponse;
import shopping.wish.adapter.in.api.WishCreateRequest;
import shopping.wish.adapter.in.api.WishResponse;

@DisplayName("[통합] 쇼핑 흐름 통합 테스트")
class ShoppingFlowE2eTest extends AbstractE2eTest {
    @Test
    @DisplayName("판매자 상품 등록부터 사용자 위시 조회까지 핵심 흐름이 이어진다")
    void shoppingFlowWorksEndToEnd() {
        TestAccount seller = seedSeller();
        AuthSession sellerSession = login(seller.email(), seller.password());
        ProductCreateRequest createRequest = new ProductCreateRequest(
                "노트북",
                "휴대용 컴퓨터",
                new BigDecimal("150000"),
                "https://example.com/laptop.png"
        );

        ResponseEntity<String> createProductResponse = post(
                "/api/products",
                createRequest,
                bearerHeaders(sellerSession.accessToken())
        );
        assertStatus(createProductResponse, HttpStatus.CREATED);
        ProductResponse productResponse = readBody(createProductResponse, ProductResponse.class);

        String userEmail = uniqueEmail("user");
        registerMember(userEmail, "password123!");
        AuthSession userSession = login(userEmail, "password123!");

        ResponseEntity<String> addWishResponse = post(
                "/api/wishes",
                new WishCreateRequest(productResponse.id(), 1),
                bearerHeaders(userSession.accessToken())
        );
        assertStatus(addWishResponse, HttpStatus.CREATED);

        AuthSession refreshedSession = refresh(userSession);
        ResponseEntity<String> wishListResponse = get("/api/wishes", bearerHeaders(refreshedSession.accessToken()));

        assertStatus(wishListResponse, HttpStatus.OK);
        List<WishResponse> wishResponses = readBody(wishListResponse, new TypeReference<>() {
        });
        assertThat(wishResponses).hasSize(1);
        assertThat(wishResponses.get(0).productName()).isEqualTo("노트북");
    }
}
