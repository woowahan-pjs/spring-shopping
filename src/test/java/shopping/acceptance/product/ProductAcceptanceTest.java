package shopping.acceptance.product;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import shopping.acceptance.utils.AcceptanceTest;

public class ProductAcceptanceTest extends AcceptanceTest {
    /**
     * when: 상품 이름, 이미지 url, 가격을 입력하면
     * then: 상품이 등록된다
     */
    @DisplayName("상품 생성 테스트")
    @Test
    void test() {
        // when
        String name = "product";
        String imageUrl = "imageUrl";
        int price = 1000;
        ExtractableResponse<Response> response = ProductSteps.상품_등록(name, imageUrl, price);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> responseOfProduct = ProductSteps.상품_단건_조회(1L);
        assertThat(ProductSteps.상품_단건_이름_추출(responseOfProduct)).isEqualTo(name);
        assertThat(ProductSteps.상품_단건_이미지_URL_추출(responseOfProduct)).isEqualTo(imageUrl);
        assertThat(ProductSteps.상품_단건_가격_추출(responseOfProduct)).isEqualTo(price);
    }

    /**
     * when: 상품 이름에 비속어가 포함되면
     * then: 400 에러가 발생한다
     */
    @DisplayName("이름에 비속어가 포함된 상품 생성 테스트")
    @Test
    void test2() {
        // when
        ExtractableResponse<Response> response = ProductSteps.상품_등록("비속어", "imageUrl", 1000);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * given: 등록된 상품이 있고
     * when: 상품의 Id로 조회하면
     * then: 해당하는 상품이 조회된다.
     */
    @DisplayName("상품 단건 조회 테스트")
    @Test
    void test3() {
        // given
        String name = "product";
        String imageUrl = "imageUrl";
        int price = 1000;
        ProductSteps.상품_등록(name, imageUrl, price);

        // when
        ExtractableResponse<Response> response = ProductSteps.상품_단건_조회(1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(ProductSteps.상품_단건_이름_추출(response)).isEqualTo(name);
        assertThat(ProductSteps.상품_단건_이미지_URL_추출(response)).isEqualTo(imageUrl);
        assertThat(ProductSteps.상품_단건_가격_추출(response)).isEqualTo(price);
    }

    /**
     * given: 등록된 상품이 있고
     * when: 상품 목록 조회를 요청하면
     * then: 페이징된 결과를 얻을 수 있다.
     */
    @DisplayName("상품 목록 조회 테스트")
    @Test
    void test4() {
        // given
        ProductSteps.상품_등록("product1", "imageUrl1", 1000);

        String name2 = "product2";
        String imageUrl2 = "imageUrl2";
        int price2 = 2000;
        ProductSteps.상품_등록(name2, imageUrl2, price2);

        // when
        ExtractableResponse<Response> response = ProductSteps.상품_목록_조회(2, 1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(ProductSteps.상품_목록_이름_추출(response)).containsOnly(name2);
        assertThat(ProductSteps.상품_목록_이미지_URL_추출(response)).containsOnly(imageUrl2);
        assertThat(ProductSteps.상품_목록_가격_추출(response)).containsOnly(price2);
    }

    /**
     * given: 등록된 상품이 있고
     * when: 수정할 이름, 이미지 url, 가격을 입력하면
     * then: 상품 정보가 수정된다
     */
    @DisplayName("상품 수정 테스트")
    @Test
    void test5() {
        // given
        ProductSteps.상품_등록("product", "imageUrl", 1000);

        // when
        String name = "updateName";
        String imageUrl = "updateUrl";
        int price = 9999;
        ExtractableResponse<Response> response = ProductSteps.상품_수정(1L, name, imageUrl, price);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> responseOfProduct = ProductSteps.상품_단건_조회(1L);
        assertThat(ProductSteps.상품_단건_이름_추출(responseOfProduct)).isEqualTo(name);
        assertThat(ProductSteps.상품_단건_이미지_URL_추출(responseOfProduct)).isEqualTo(imageUrl);
        assertThat(ProductSteps.상품_단건_가격_추출(responseOfProduct)).isEqualTo(price);
    }


    /**
     * given: 등록된 상품이 있고
     * when: 상품의 id로 삭제를 요청하면
     * then: 상품이 삭제된다.
     */
    @DisplayName("상품 삭제 테스트")
    @Test
    void test6() {
        // given
        ProductSteps.상품_등록("product", "imageUrl", 1000);

        // when
        ExtractableResponse<Response> response = ProductSteps.상품_삭제(1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> responseOfProduct = ProductSteps.상품_단건_조회(1L);
        assertThat(responseOfProduct.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
