package shopping.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shopping.auth.AdminInterceptor;
import shopping.auth.JwtTokenProvider;
import shopping.member.domain.MemberRole;
import shopping.product.domain.Product;
import shopping.product.controller.dto.ProductRequest;
import shopping.product.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shopping.product.domain.ProductFixture.*;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;


@WebMvcTest(ProductController.class)
@Import(AdminInterceptor.class)
@AutoConfigureRestDocs
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ProductService service;

    @MockitoBean
    JwtTokenProvider provider;

    @BeforeEach
    void setUp() {
        given(provider.extractRole(any())).willReturn(MemberRole.ADMIN);
    }

    @Test
    @DisplayName("상품을 추가한다.")
    void addProduct() throws Exception {
        Product product = createWithId(1L);
        ProductRequest request = new ProductRequest(VALID_NAME, VALID_PRICE, VALID_IMAGE_URL);

        given(service.save(any())).willReturn(product);

        mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andDo(document("products/add",
                                requestHeaders(
                                        headerWithName("Authorization").description("Bearer JWT 토큰")
                                ),
                                requestFields(
                                    fieldWithPath("name").description("상품명"),
                                    fieldWithPath("price").description("상품가격"),
                                    fieldWithPath("imageUrl").description("상품이미지URL")
                                )
                        ));
    }

    @Test
    @DisplayName("상품 목록을 가져온다.")
    void findAll() throws Exception {
        List<Product> products = List.of(createWithId(1L), createWithId(2L), createWithId(3L));
        Pageable pageable = PageRequest.of(0, 20);
        PageImpl<Product> page = new PageImpl<>(products, pageable, products.size());

        given(service.findProducts(any())).willReturn(page);

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpectAll(status().isOk(),
                        jsonPath("$.totalElements").value(3))
                .andDo(document("products/find-all",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작, 기본값: 0)").optional(),
                                parameterWithName("size").description("페이지 크기 (기본값: 20)").optional()
                        ),
                        responseFields(
                                fieldWithPath("contents[].id").description("상품 ID"),
                                fieldWithPath("contents[].name").description("상품명"),
                                fieldWithPath("contents[].price").description("상품가격"),
                                fieldWithPath("contents[].imageUrl").description("상품이미지경로"),
                                fieldWithPath("page").description("현재 페이지 번호"),
                                fieldWithPath("size").description("페이지 크기"),
                                fieldWithPath("totalElements").description("전체 상품 수"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("hasNext").description("다음 페이지 존재 여부")
                        )
                ));
    }

    @Test
    @DisplayName("특정 상품을 조회한다")
    void findById() throws Exception {
        given(service.findProductById(1L)).willReturn(createWithId(1L));

        mockMvc.perform(get("/products/{id}", 1L))
                .andExpectAll(status().isOk(),
                        jsonPath("$.id").value(1L))
                .andDo(document("products/find-by-id",
                        pathParameters(
                                parameterWithName("id").description("조회할 상품 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("상품 ID"),
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("price").description("상품가격"),
                                fieldWithPath("imageUrl").description("상품이미지경로")
                        )
                ));
    }

    @Test
    @DisplayName("상품을 수정한다")
    void update() throws Exception {
        String changeName = "치킨";
        String changeImageUrl = "http://pizzanara.com/a.jpg";
        BigDecimal changePrice = new BigDecimal(100000);

        Product product = createWithId(1L);
        product.changeName(changeName);
        product.changePrice(changePrice);
        product.changeImageUrl(changeImageUrl);

        ProductRequest request = new ProductRequest( "치킨", changePrice, changeImageUrl);

        given(service.update(eq(1L), any())).willReturn(product);

        mockMvc.perform(put("/products/{id}", 1L)
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk(),
                        jsonPath("$.name").value(changeName),
                        jsonPath("$.price").value(changePrice),
                        jsonPath("$.imageUrl").value(changeImageUrl))
                .andDo(document("products/update",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("price").description("상품가격"),
                                fieldWithPath("imageUrl").description("상품이미지경로")
                        ),
                        pathParameters(
                                parameterWithName("id").description("수정할 상품 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("상품 ID"),
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("price").description("상품가격"),
                                fieldWithPath("imageUrl").description("상품이미지경로")
                        )
                ));
    }

    @Test
    @DisplayName("상품을 삭제한다")
    void deleteProduct() throws Exception {
        willDoNothing().given(service).deleteById(1L);

        mockMvc.perform(delete("/products/{id}", 1L)
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isNoContent())
                .andDo(document("products/delete",
                        pathParameters(
                                parameterWithName("id").description("삭제할 상품 ID")
                        )
                ));
    }

    @Test
    @DisplayName("유효하지 않은 상품 추가 시 예외 발생")
    void addProduct_InvalidName() throws Exception {
        ProductRequest request = new ProductRequest("", VALID_PRICE, VALID_IMAGE_URL);

        mockMvc.perform(post("/products")
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않은 상품을 조회 시 예외 발생")
    void findById_notFound() throws Exception {
        willThrow(new NoSuchElementException()).given(service).findProductById(1L);

        mockMvc.perform(get("/products/{id}", 1))
                        .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("관리자가 아니면 상품 등록 불가")
    void addProduct_forbidden() throws Exception {
        ProductRequest request = new ProductRequest("", VALID_PRICE, VALID_IMAGE_URL);

        given(provider.extractRole(any())).willReturn(MemberRole.USER); // 이 테스트에서만 오버라이드

        mockMvc.perform(post("/products")
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }
}