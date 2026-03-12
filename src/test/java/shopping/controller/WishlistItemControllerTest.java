package shopping.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import shopping.auth.AuthInterceptor;
import shopping.auth.JwtTokenProvider;
import shopping.controller.dto.WishlistItemRequest;
import shopping.controller.dto.WishlistItemResponse;
import shopping.service.WishlistItemService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static shopping.domain.WishlistItemFixture.*;


@WebMvcTest(WishlistItemController.class)
@Import(AuthInterceptor.class)
class WishlistItemControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    WishlistItemService service;

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
    void addWishlistItem() throws JsonProcessingException {
        WishlistItemRequest request = new WishlistItemRequest(1L);

        willDoNothing().given(service).addWishlistItem(any());

        assertThat(authenticatedPost("/wishlist")
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("위시리스트를 조회한다")
    void findAllByMemberId() throws JsonProcessingException {
        given(service.findWishlistItems(eq(VALID_MEMBER_ID))).willReturn(List.of(createWishItem(1L, 1L), createWishItem(2L, 2L)));

        assertThat(mockMvcTester.get().uri("/wishlist")
                .header("Authorization", "Bearer valid-token"))
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .convertTo(InstanceOfAssertFactories.list(WishlistItemResponse.class))
                .hasSize(2);
    }

    @Test
    @DisplayName("위시리스트 상품을 삭제한다")
    void deleteWishlistItemById() {
        willDoNothing().given(service).deleteWishlistItem(1L, 1L);

        assertThat(mockMvcTester.delete().uri("/wishlist/1")
                .header("Authorization", "Bearer valid-token"))
                .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("위시리스트 존재하지않으면 예외 발생")
    void deleteWishlistItemById_invalidMemberId() {
        willThrow(new IllegalArgumentException()).given(service).deleteWishlistItem(1L, 1L);

        assertThat(mockMvcTester.delete().uri("/wishlist/1")
                .header("Authorization", "Bearer valid-token"))
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("로그인 안하면 예외 발생")
    void notLogin() {
        assertThat(mockMvcTester.get().uri("/wishlist"))
                .hasStatus(HttpStatus.UNAUTHORIZED);
    }

    private MockMvcTester.MockMvcRequestBuilder authenticatedPost(String url) {
        return mockMvcTester.post().uri(url)
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON);
    }
}