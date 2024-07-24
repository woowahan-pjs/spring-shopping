package shopping.product;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import shopping.AcceptanceTest;
import shopping.member.dto.MemberResponse;
import shopping.product.dto.ProductResponse;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static shopping.product.ProductAcceptanceStepTest.*;


@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    private static final String prdctnm1 = "[맛있는 음식]v1";
    private static final BigDecimal price1 = new BigDecimal(25000);

    private static final String prdctnm2 = "swtich";
    private static final BigDecimal price2 = new BigDecimal(375000);

    private static final String prdctnm3 = "배달의 민족 맛집";
    private static final String imgaes3 = "https://";
    private static final BigDecimal price3 = new BigDecimal(14500);


    @DisplayName("상품을 등록한다.")
    @Test
    void createProduct() {
        // when
        ExtractableResponse<Response> response = createProductRequest(prdctnm1, price1);

        // then
        createdProduct(response);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void getProducts() {
        // given
        ExtractableResponse<Response> createResponse1 = alreadyCreatedProduct(prdctnm1, price1);
        ExtractableResponse<Response> createResponse2 = alreadyCreatedProduct(prdctnm2, price2);
        ExtractableResponse<Response> createResponse3 = alreadyCreatedProduct(prdctnm3, price3, imgaes3);

        // when
        ExtractableResponse<Response> response = findProductListRequest();

        // then
        findMemberListResponse(response);
        containProductList(response, Arrays.asList(createResponse1, createResponse2, createResponse3));
    }



    public static ExtractableResponse<Response> alreadyCreatedProduct(String name, BigDecimal price) {
        return createProductRequest(name, price);
    }

    public static ExtractableResponse<Response> alreadyCreatedProduct(String name, BigDecimal price, String image) {
        return createProductRequest(name, price);
    }

    public static void findMemberListResponse(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    public static void containProductList(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedPrdctsSns = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<ProductResponse.ProductDetail> resultProducts = response.jsonPath().getList("products", ProductResponse.ProductDetail.class);
        List<Long> resultProductSns = resultProducts.stream().map(ProductResponse.ProductDetail::getPrdctSn).toList();

        assertThat(resultProductSns).containsAll(expectedPrdctsSns);
    }


}