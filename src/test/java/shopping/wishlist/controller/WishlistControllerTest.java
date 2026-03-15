package shopping.wishlist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shopping.auth.AuthInterceptor;
import shopping.auth.JwtTokenProvider;
import shopping.wishlist.controller.dto.WishlistRequest;
import shopping.wishlist.service.WishlistService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shopping.wishlist.domain.WishlistFixture.*;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;


@WebMvcTest(WishlistController.class)
@Import(AuthInterceptor.class)
@AutoConfigureRestDocs
class WishlistControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    WishlistService service;

    @MockitoBean
    JwtTokenProvider tokenProvider;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        given(tokenProvider.extract(any())).willReturn(VALID_MEMBER_ID);
    }

    @Test
    @DisplayName("위시리스트를 추가한다")
    void addWishlistItem() throws Exception {
        WishlistRequest request = new WishlistRequest(1L);

        willDoNothing().given(service).addWishlist(any());

        mockMvc.perform(post("/wishlist")
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("wishlist/add",
                    requestHeaders(
                            headerWithName("Authorization").description("Bearer JWT 토큰")
                    ),
                    requestFields(
                            fieldWithPath("productId").description("추가할 상품ID")
                    )
                ));
    }

    @Test
    @DisplayName("위시리스트를 조회한다")
    void findAllByMemberId() throws Exception {
        given(service.findWishlistItems(eq(VALID_MEMBER_ID))).willReturn(List.of(createWish(1L, 1L), createWish(2L, 2L)));

        mockMvc.perform(get("/wishlist")
                .header("Authorization", "Bearer valid-token"))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$", hasSize(2))
                ).andDo(document("wishlist/find-all",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 토큰")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("위시리스트 항목 ID"),
                                fieldWithPath("[].productId").description("상품 ID")
                        )
                ));
    }

    @Test
    @DisplayName("위시리스트 상품을 삭제한다")
    void deleteWishlistItemById() throws Exception {
        willDoNothing().given(service).deleteWishlistItem(1L, 1L);

        mockMvc.perform(delete("/wishlist/{id}", 1L)
                .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isNoContent())
                .andDo(document("wishlist/delete",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("삭제할 위시리스트 항목 ID")
                        )
                ));
    }

    @Test
    @DisplayName("위시리스트 존재하지않으면 예외 발생")
    void deleteWishlistItemById_invalidMemberId() throws Exception {
        willThrow(new IllegalArgumentException()).given(service).deleteWishlistItem(1L, 1L);

        mockMvc.perform(delete("/wishlist/{id}", 1L)
                .header("Authorization", "Bearer valid-token"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("로그인 안하면 예외 발생")
    void notLogin() throws Exception {
        mockMvc.perform(get("/wishlist"))
                .andExpect(status().is4xxClientError());
    }
}