package shopping.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import shopping.common.ErrorResponse;
import shopping.e2e.support.AbstractE2eTest;
import shopping.e2e.support.AuthSession;
import shopping.e2e.support.TestAccount;
import shopping.product.domain.Product;
import shopping.wish.adapter.in.api.WishCreateRequest;
import shopping.wish.adapter.in.api.WishResponse;

@DisplayName("[위시] 위시 통합 테스트")
class WishE2eTest extends AbstractE2eTest {
    @Nested
    @DisplayName("추가")
    class Add {
        @Test
        @DisplayName("로그인한 사용자는 상품을 위시에 추가할 수 있다")
        void memberCanAddWish() {
            TestAccount seller = seedSeller();
            Product product = seedProduct(seller.memberId(), "Notebook");
            AuthSession userSession = registerMember(uniqueEmail("user"), "password123!");

            ResponseEntity<String> response = post(
                    "/api/wishes",
                    new WishCreateRequest(product.getId(), 2),
                    bearerHeaders(userSession.accessToken())
            );

            assertStatus(response, HttpStatus.CREATED);
            WishResponse wishResponse = readBody(response, WishResponse.class);
            assertThat(wishResponse.productId()).isEqualTo(product.getId());
            assertThat(wishResponse.quantity()).isEqualTo(2);
        }

        @Test
        @DisplayName("수량을 비우면 기본 수량 1로 저장한다")
        void addWishUseDefaultQuantityWhenQuantityIsMissing() {
            TestAccount seller = seedSeller();
            Product product = seedProduct(seller.memberId(), "Notebook");
            AuthSession userSession = registerMember(uniqueEmail("user"), "password123!");
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("productId", product.getId());

            ResponseEntity<String> response = post(
                    "/api/wishes",
                    requestBody,
                    bearerHeaders(userSession.accessToken())
            );

            assertStatus(response, HttpStatus.CREATED);
            WishResponse wishResponse = readBody(response, WishResponse.class);
            assertThat(wishResponse.quantity()).isEqualTo(1);
        }

        @Test
        @DisplayName("같은 상품을 중복으로 추가하면 409를 반환한다")
        void addWishRejectDuplicateProduct() {
            TestAccount seller = seedSeller();
            Product product = seedProduct(seller.memberId(), "Notebook");
            AuthSession userSession = registerMember(uniqueEmail("user"), "password123!");
            post(
                    "/api/wishes",
                    new WishCreateRequest(product.getId(), 1),
                    bearerHeaders(userSession.accessToken())
            );

            ResponseEntity<String> response = post(
                    "/api/wishes",
                    new WishCreateRequest(product.getId(), 1),
                    bearerHeaders(userSession.accessToken())
            );

            assertStatus(response, HttpStatus.CONFLICT);
            ErrorResponse errorResponse = readError(response);
            assertThat(errorResponse.code()).isEqualTo("WISH_ALREADY_EXISTS");
        }
    }

    @Nested
    @DisplayName("조회 및 삭제")
    class ListAndDelete {
        @Test
        @DisplayName("위시 목록은 현재 사용자 기준으로만 반환한다")
        void listReturnOnlyCurrentMemberWishes() {
            TestAccount seller = seedSeller();
            Product product = seedProduct(seller.memberId(), "Notebook");
            AuthSession userOne = registerMember(uniqueEmail("user"), "password123!");
            AuthSession userTwo = registerMember(uniqueEmail("user"), "password123!");
            post("/api/wishes", new WishCreateRequest(product.getId(), 1), bearerHeaders(userOne.accessToken()));
            post("/api/wishes", new WishCreateRequest(product.getId(), 2), bearerHeaders(userTwo.accessToken()));

            ResponseEntity<String> response = get("/api/wishes", bearerHeaders(userOne.accessToken()));

            assertStatus(response, HttpStatus.OK);
            List<WishResponse> wishResponses = readBody(response, new TypeReference<>() {
            });
            assertThat(wishResponses).hasSize(1);
            assertThat(wishResponses.get(0).quantity()).isEqualTo(1);
        }

        @Test
        @DisplayName("위시 삭제는 정상 동작한다")
        void deleteWishSuccess() {
            TestAccount seller = seedSeller();
            Product product = seedProduct(seller.memberId(), "Notebook");
            AuthSession userSession = registerMember(uniqueEmail("user"), "password123!");
            ResponseEntity<String> createResponse = post(
                    "/api/wishes",
                    new WishCreateRequest(product.getId(), 1),
                    bearerHeaders(userSession.accessToken())
            );
            WishResponse wishResponse = readBody(createResponse, WishResponse.class);

            ResponseEntity<String> deleteResponse = delete(
                    "/api/wishes/" + wishResponse.wishId(),
                    bearerHeaders(userSession.accessToken())
            );

            assertStatus(deleteResponse, HttpStatus.NO_CONTENT);
            ResponseEntity<String> listResponse = get("/api/wishes", bearerHeaders(userSession.accessToken()));
            List<WishResponse> wishResponses = readBody(listResponse, new TypeReference<>() {
            });
            assertThat(wishResponses).isEmpty();
        }

        @Test
        @DisplayName("인증 없이 위시 목록을 조회하면 401을 반환한다")
        void wishApiRejectGuest() {
            ResponseEntity<String> response = get("/api/wishes", jsonHeaders());

            assertStatus(response, HttpStatus.UNAUTHORIZED);
            ErrorResponse errorResponse = readError(response);
            assertThat(errorResponse.code()).isEqualTo("AUTH_HEADER_REQUIRED");
        }
    }
}
