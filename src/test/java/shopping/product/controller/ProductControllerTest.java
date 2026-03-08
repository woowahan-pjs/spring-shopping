package shopping.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import shopping.product.dto.ProductCreateRequest;
import shopping.product.dto.ProductResponse;
import shopping.product.dto.ProductUpdateRequest;
import shopping.product.service.ProductService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 상품을_생성한다() throws Exception {

        ProductCreateRequest request = new ProductCreateRequest("아이폰", 1_000_000);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(productService).createProduct(any());
    }

    @Test
    void 특정_상품을_조회한다() throws Exception {

        ProductResponse response = new ProductResponse(1L, "아이폰", 1_000_000);

        given(productService.getProduct(1L))
                .willReturn(response);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("아이폰"))
                .andExpect(jsonPath("$.price").value(1_000_000));
    }

    @Test
    void 상품을_수정한다() throws Exception {

        ProductUpdateRequest request = new ProductUpdateRequest();

        ReflectionTestUtils.setField(request, "name", "아이폰16");
        ReflectionTestUtils.setField(request, "price", 2_000_000);
        ReflectionTestUtils.setField(request, "imageUrl", "new_iphone.jpg");

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(productService).updateProduct(eq(1L), any());
    }

    @Test
    void 상품을_삭제한다() throws Exception {

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());

        verify(productService).deleteProduct(1L);
    }
}
