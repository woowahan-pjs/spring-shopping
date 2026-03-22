package shopping.wish.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shopping.wish.dto.WishProductResponse;
import shopping.wish.service.WishService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WishController.class)
@AutoConfigureMockMvc(addFilters = false)
public class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WishService wishService;

    @Test
    void 위시리스트에_상품을_추가한다() throws Exception {

        mockMvc.perform(post("/api/wishes")
                        .header("memberId", "1")
                        .param("productId", "1"))
                .andExpect(status().isOk());

        verify(wishService).addWish(1L, 1L);
    }

    @Test
    void 위시리스트_아이템을_삭제한다() throws Exception {

        mockMvc.perform(delete("/api/wishes/{wishId}", 1L))
                .andExpect(status().isOk());

        verify(wishService).deleteWish(1L, 1L);
    }

    @Test
    void 위시리스트를_조회한다() throws Exception {

        WishProductResponse response = new WishProductResponse(1L, 1L, "아이폰", 1_000_000, "image.jpg");

        given(wishService.getWishList(anyLong()))
                .willReturn(List.of(response));

        mockMvc.perform(get("/api/wishes")
                        .header("memberId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L));
    }
}
