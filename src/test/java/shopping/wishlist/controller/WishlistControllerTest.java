package shopping.wishlist.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import shopping.support.ControllerTestSupport;
import shopping.wishlist.controller.dto.WishlistRequest;
import shopping.wishlist.domain.Wishlist;

import java.util.List;

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

@DisplayName("위시리스트 API")
class WishlistControllerTest extends ControllerTestSupport {

    @BeforeEach
    void setUp() {
        given(provider.extract(any())).willReturn(VALID_MEMBER_ID);
    }

    @Test
    @DisplayName("위시리스트를 추가한다")
    void addWishlistItem() throws Exception {
        WishlistRequest request = new WishlistRequest(1L);

        willDoNothing().given(wishlistService).addWishlist(any());

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
        Pageable pageable = PageRequest.of(0, 20);
        List<Wishlist> wishlist = List.of(createWish(1L, 1L), createWish(2L, 2L));
        PageImpl<Wishlist> page = new PageImpl<>(wishlist, pageable, wishlist.size());

        given(wishlistService.findWishlistItems(eq(VALID_MEMBER_ID), any())).willReturn(page);

        mockMvc.perform(get("/wishlist")
                        .param("page", "0")
                        .param("size", "20")
                .header("Authorization", "Bearer valid-token"))
                .andExpectAll(status().isOk(),
                        jsonPath("$.totalElements").value(2)
                ).andDo(document("wishlist/find-all",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작, 기본값: 0)").optional(),
                                parameterWithName("size").description("페이지 크기 (기본값: 20)").optional()
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 토큰")
                        ),
                        responseFields(
                                fieldWithPath("contents[].id").description("위시리스트 항목 ID"),
                                fieldWithPath("contents[].productId").description("상품 ID"),
                                fieldWithPath("page").description("현재 페이지 번호"),
                                fieldWithPath("size").description("페이지 크기"),
                                fieldWithPath("totalElements").description("전체 항목 수"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("hasNext").description("다음 페이지 존재 여부")
                        )
                ));
    }

    @Test
    @DisplayName("위시리스트 상품을 삭제한다")
    void deleteWishlistItemById() throws Exception {
        willDoNothing().given(wishlistService).deleteWishlistItem(1L, 1L);

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
        willThrow(new IllegalArgumentException()).given(wishlistService).deleteWishlistItem(1L, 1L);

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