package shopping.acceptance.category.steps;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.category.api.dto.CategoryRegistrationHttpRequest;
import shopping.category.api.dto.SubCategoryRegistrationHttpRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static shopping.utils.fixture.CategoryFixture.NAME;
import static shopping.utils.fixture.CategoryFixture.ORDER;

public class CategoryAcceptanceSteps {
    private static final String USER_BASE_URL = "/api/categories";

    public static ExtractableResponse<Response> registerMain(final String name, final int order, final String accessToken) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", "Bearer " + accessToken))
                .body(new CategoryRegistrationHttpRequest(name, order))
                .when()
                .post(USER_BASE_URL)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> registerSub(final String name, final int order, final long mainCategoryId, final String accessToken) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", "Bearer " + accessToken))
                .body(new SubCategoryRegistrationHttpRequest(name, order))
                .when()
                .post(USER_BASE_URL + "/" + mainCategoryId + "/sub")
                .then()
                .extract();
    }

    public static void validate(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void validateDuplicatedName(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateInvalidCustomer(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateInvalidSeller(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static long 메인카테고리생성됨(final String name, final int order, final String accessToken) {
        final ExtractableResponse<Response> response = CategoryAcceptanceSteps.registerMain(NAME, ORDER, accessToken);
        CategoryAcceptanceSteps.validate(response);
        return response.body().jsonPath().getLong("id");
    }
}
