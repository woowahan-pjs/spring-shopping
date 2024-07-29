package shopping.product.controller

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.kotlin.any
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import shopping.E2ETest
import shopping.common.error.ErrorCode
import shopping.product.application.CreateProductRequest
import shopping.product.application.INVALID_NAME_LENGTH_MESSAGE
import shopping.product.application.INVALID_NAME_PATTERN_MESSAGE
import shopping.product.domain.BadWordValidator

class ProductControllerTest(
    private val badWordValidator: BadWordValidator,
) : E2ETest() {
    @Test
    fun `상품 등록 성공`() {
        val request = CreateProductRequestFixture().build()
        val response = `상품 생성 요청`(request)

        assertSoftly { softly ->
            softly.assertThat(response.getLong("data.id")).isNotNull
            softly.assertThat(response.getString("data.name")).isEqualTo(request.name)
            softly.assertThat(response.getString("data.imageUrl")).isEqualTo(request.imageUrl)
        }
    }

    @ValueSource(strings = ["", "1234512345123451"])
    @ParameterizedTest(name = "상품명: {0}")
    fun `상품이름 길이를 만족해야한다`(productName: String) {
        val request = CreateProductRequestFixture(name = productName).build()
        val response = `상품 생성 요청`(request)

        assertThat(response.getString("error.data[0].message")).isEqualTo(INVALID_NAME_LENGTH_MESSAGE)
    }

    @Test
    fun `허용되지 않는 특수문자는 등록할수 없다`() {
        val request = CreateProductRequestFixture(name = "product !").build()
        val response = `상품 생성 요청`(request)

        assertThat(response.getString("error.data[0].message")).isEqualTo(INVALID_NAME_PATTERN_MESSAGE)
    }

    @Test
    fun `이름에 영문 비속어가 있으면 등록할수 없다`() {
        whenever(badWordValidator.isBadWord(any())).thenReturn(true)

        val response = `상품 생성 요청`(CreateProductRequestFixture().build())

        assertThat(response.getString("error.code")).isEqualTo(ErrorCode.PRODUCT_NAME_CONTAIN_BAD_WORD.code)
        reset(badWordValidator)
    }

    private fun `상품 생성 요청`(request: CreateProductRequest) =
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/products")
            .then()
            .extract()
            .jsonPath()
}
