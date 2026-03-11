package shopping.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
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
import shopping.service.WishlistItemService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;


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

    private static final Long VALID_MEMBER_ID = 1L;

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

    private MockMvcTester.MockMvcRequestBuilder authenticatedPost(String url) {
        return mockMvcTester.post().uri(url)
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON);
    }
}