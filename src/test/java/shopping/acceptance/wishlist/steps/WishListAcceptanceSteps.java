package shopping.acceptance.wishlist.steps;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.wishlist.infrastructure.api.dto.WishListRegistrationRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class WishListAcceptanceSteps {

    private static final String WISH_LIST_BASE_URL = "/api/wish-lists";

    public static ExtractableResponse<Response> registerWishlist(final long productId, final String customerAccessToken) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", "Bearer " + customerAccessToken))
                .body(new WishListRegistrationRequest(productId))
                .when()
                .post(WISH_LIST_BASE_URL)
                .then()
                .extract();
    }

    public static void validate(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static long 등록됨(final long 상품, final String customerAccessToken) {
        final ExtractableResponse<Response> response = registerWishlist(상품, customerAccessToken);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.body().jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 목록조회(final String customerAccessToken) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", "Bearer " + customerAccessToken))
                .when()
                .get(WISH_LIST_BASE_URL)
                .then()
                .extract();
    }

    public static void validateList(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getList("wish-lists")).isNotEmpty()
        );
    }
}
