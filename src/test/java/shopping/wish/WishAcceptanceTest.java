package shopping.wish;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import shopping.AcceptanceTest;
import shopping.auth.AuthAcceptanceStepTest;
import shopping.auth.dto.TokenResponse;
import shopping.member.MemberAcceptanceTest;
import shopping.member.dto.MemberResponse;
import shopping.product.ProductAcceptanceTest;
import shopping.product.dto.ProductResponse;
import shopping.wishlist.dto.WishRequest;
import shopping.wishlist.dto.WishResponse;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static shopping.wish.WishAcceptanceStepTest.*;


@DisplayName("위시 관련 기능")
public class WishAcceptanceTest extends AcceptanceTest{

    private static final String prdctNm1 = "[맛있는 음식]v1";
    private static final BigDecimal price1 = new BigDecimal(25000);

    private static final String prdctNm3 = "배달의 민족 맛집";
    private static final String images3 = "https://";
    private static final BigDecimal price3 = new BigDecimal(14500);

    private static final String password1 = "1234";
    private static final String email1 = "yalmung@email.com";
    private static final String name1 = "얄뭉";

    ProductResponse.ProductDetail productResponse1;
    ProductResponse.ProductDetail productResponse2;
    MemberResponse.MemberDetail memberResponse;
    TokenResponse tokenResponse;
    private static String token;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        productResponse1 = ProductAcceptanceTest.alreadyCreatedProduct(prdctNm1, price1).as(ProductResponse.ProductDetail.class);
        productResponse2 = ProductAcceptanceTest.alreadyCreatedProduct(prdctNm3, price3, images3).as(ProductResponse.ProductDetail.class);
        memberResponse = MemberAcceptanceTest.alreadyCreatedMember(password1, email1, name1, null).as(MemberResponse.MemberDetail.class);
        tokenResponse = AuthAcceptanceStepTest.login(email1, password1).as(TokenResponse.class);
        token = tokenResponse.getAccessToken();
    }


    @DisplayName("위시를 등록한다.")
    @Test
    void createWish() {
        // when
        ExtractableResponse<Response> response = createWishRequest(token, productResponse1.getPrdctSn());

        // then
        createdWish(response);
    }

    @DisplayName("위시 목록을 조회한다.")
    @Test
    void getMemberWishList() {
        // given
        ExtractableResponse<Response> createResponse1 = alreadyCreatedWish(token, productResponse1.getPrdctSn());
        ExtractableResponse<Response> createResponse2 = alreadyCreatedWish(token, productResponse2.getPrdctSn());

        // when
        ExtractableResponse<Response> response = findWishListRequest(token);

        // then
        findWishListResponse(response);
        containWishList(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("위시 수량을 수정한다.")
    @Test
    void updateWishProductCnt() {
        // given
        WishResponse.WishDetail createResponse = alreadyCreatedWish(token, productResponse1.getPrdctSn()).as(WishResponse.WishDetail.class);

        // when
        ExtractableResponse<Response> response = modifyWishProductCntRequest(token, createResponse.getWishSn(), WishRequest.ModWishProductCnt.from(1L, true));

        // then
        validateModWishProductCnt(response);
    }

    @DisplayName("위시 상품을 삭제한다.")
    @Test
    void deleteWishProduct() {
        // given
        ExtractableResponse<Response> createResponse1 = alreadyCreatedWish(token, productResponse1.getPrdctSn());
        ExtractableResponse<Response> createResponse2 = alreadyCreatedWish(token, productResponse2.getPrdctSn());

        // when
        ExtractableResponse<Response> response = deleteWishProductRequest(token, createResponse1.as(WishResponse.WishDetail.class).getWishSn());

        // then
        int wishCnt = List.of(createResponse1, createResponse2).size();
        WishResponse.WishListRes memberWishResponse = findWishListRequest(token).as(WishResponse.WishListRes.class);
        validateDeleteWishProduct(response, memberWishResponse, wishCnt -1);
    }


    private ExtractableResponse<Response> alreadyCreatedWish(String token, Long prdctSn) {
        return createWishRequest(token, prdctSn);
    }

    private void findWishListResponse(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void createdWish(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void containWishList(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedWishSns = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<WishResponse.WishWithProductDetail> resultWishList = response.jsonPath().getList("wishList", WishResponse.WishWithProductDetail.class);
        List<Long> resultWishListSns = resultWishList.stream().map(WishResponse.WishWithProductDetail::getWishSn).toList();

        assertThat(resultWishListSns).containsAll(expectedWishSns);
    }

    private void validateModWishProductCnt(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void validateDeleteWishProduct(ExtractableResponse<Response> response, WishResponse.WishListRes memberWishResponse, int wishCnt) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(memberWishResponse.getTotalCnt()).isEqualTo(wishCnt)
        );
    }
}
