package shopping.wish;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.auth.domain.LoginMember;
import shopping.product.dto.ProductRequest;
import shopping.product.dto.ProductResponse;
import shopping.wishlist.dto.WishRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


public class WishAcceptanceStepTest {
    public static ExtractableResponse<Response> createWishRequest(String token, Long prdctSn) {
        WishRequest.RegWishList regWishRequest = WishRequest.RegWishList.from(prdctSn);

        return RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .body(regWishRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/wishList")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> findWishListRequest(String token) {
        return RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .when().get("/wishList/member")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> modifyWishProductCntRequest(String token, Long wishSn, WishRequest.ModWishProductCnt params) {
        return RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/wishList/cnt/{id}", wishSn)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteWishProductRequest(String token, Long wishSn) {
        return RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/wishList/{id}", wishSn)
                .then().log().all()
                .extract();
    }
}
