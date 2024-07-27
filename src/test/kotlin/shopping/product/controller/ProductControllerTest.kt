package shopping.product.controller

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.boot.test.mock.mockito.MockBean
import shopping.E2ETest
import shopping.product.application.INVALID_NAME_LENGTH_MESSAGE
import shopping.product.application.INVALID_NAME_PATTERN_MESSAGE
import shopping.product.domain.BadWordValidator

class ProductControllerTest(
    @MockBean private val badWordValidator: BadWordValidator,
) : E2ETest() {
    @Test
    fun `상품 등록 성공`() {
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(CreateProductRequestFixture().build())
            .`when`()
            .post("/products")
            .then()
            .statusCode(200)
    }

    @ValueSource(strings = ["", "1234512345123451"])
    @ParameterizedTest(name = "상품명: {0}")
    fun `상품이름 길이를 만족해야한다`(productName: String) {
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(
                CreateProductRequestFixture(
                    name = productName,
                ).build(),
            ).`when`()
            .post("/products")
            .then()
            .statusCode(400)
            .body("error.data[0].message", equalTo(INVALID_NAME_LENGTH_MESSAGE))
    }

    @Test
    fun `허용되지 않는 특수문자는 등록할수 없다`() {
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(
                CreateProductRequestFixture(
                    name = "product !",
                ).build(),
            ).`when`()
            .post("/products")
            .then()
            .statusCode(400)
            .body("error.data[0].message", equalTo(INVALID_NAME_PATTERN_MESSAGE))
    }

    @Test
    fun `이름에 영문 비속어가 있으면 등록할수 없다`() {
        whenever(badWordValidator.isBadWord(any())).thenReturn(true)
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(CreateProductRequestFixture().build())
            .`when`()
            .post("/products")
            .then()
            .statusCode(400)
            .log()
            .all()
    }
}
