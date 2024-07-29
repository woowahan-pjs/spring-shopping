package shopping.wishlist.controller

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.path.json.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import shopping.LoggedInE2ETest
import shopping.common.error.ErrorCode
import shopping.product.controller.CreateProductRequestFixture
import shopping.wishlist.application.AddWishlistRequest
import shopping.wishlist.application.WishlistProductDto

class WishlistControllerTest : LoggedInE2ETest() {
    @Test
    fun `로그인하지 않으면 위시리스트 기능 사용불가능`() {
        RestAssured
            .given()
            .`when`()
            .get("/wishlists")
            .then()
            .statusCode(401)
    }

    @Test
    fun `위시리스트 상품 추가`() {
        val productId = `상품 생성`()

        val response = `위시리스트 등록 요청`(AddWishlistRequest(productId))

        assertThat(response.getLong("data.productId")).isEqualTo(productId)
    }

    @Test
    fun `없는 상품은 위시리스트에 추가할수없다`() {
        val response = `위시리스트 등록 요청`(AddWishlistRequest(1L))

        assertThat(response.getString("error.code")).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND.code)
    }

    @Test
    fun `위시리스트 조회`() {
        val productIds = listOf(`상품 생성`(), `상품 생성`())
        productIds.forEach {
            `위시리스트 등록 요청`(AddWishlistRequest(it))
        }

        val response = `위시리스트 조회 요청`()

        val list = response.getList("data.content", WishlistProductDto::class.java)
        assertThat(list.map { it.productId }).containsAll(productIds)
    }

    @Test
    fun `위시리스트 상품 삭제`() {
        val productId = `상품 생성`()
        `위시리스트 등록 요청`(AddWishlistRequest(productId))

        `위시리스트 삭제 요청`(productId)
        val response = `위시리스트 조회 요청`()

        assertThat(response.getList("data.content", WishlistProductDto::class.java)).isEmpty()
    }

    private fun `위시리스트 조회 요청`(): JsonPath =
        RestAssured
            .given()
            .auth()
            .oauth2(accessToken)
            .`when`()
            .get("/wishlists")
            .then()
            .extract()
            .jsonPath()

    private fun `위시리스트 삭제 요청`(productId: Long): JsonPath =
        RestAssured
            .given()
            .auth()
            .oauth2(accessToken)
            .`when`()
            .delete("/wishlists/$productId")
            .then()
            .log()
            .all()
            .extract()
            .jsonPath()

    private fun `위시리스트 등록 요청`(request: AddWishlistRequest): JsonPath =
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .auth()
            .oauth2(accessToken)
            .body(request)
            .`when`()
            .post("/wishlists")
            .then()
            .extract()
            .jsonPath()

    private fun `상품 생성`(): Long =
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(CreateProductRequestFixture().build())
            .`when`()
            .post("/products")
            .then()
            .extract()
            .jsonPath()
            .getLong("data.id")
}
