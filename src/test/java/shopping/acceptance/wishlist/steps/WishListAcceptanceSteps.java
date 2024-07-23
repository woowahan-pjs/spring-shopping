package shopping.acceptance.wishlist.steps;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.wishlist.infrastructure.api.dto.WishListRegistrationRequest;

import static org.assertj.core.api.Assertions.assertThat;

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
}
