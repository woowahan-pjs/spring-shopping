package shopping.product.controller;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import helper.ConverterHelper;
import helper.WithAuthUser;
import shopping.infra.exception.ShoppingBusinessException;
import shopping.infra.security.TestSecurityConfig;
import shopping.product.dto.ProductResponse;
import shopping.product.service.ProductService;

@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired @MockitoBean private ProductService productService;

    @Autowired private WebApplicationContext context;

    @BeforeEach
    void init() {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(context)
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .alwaysDo(print())
                        .build();
    }

    @Nested
    @DisplayName("상품을 조회할 때,")
    class getProduct {

        @Nested
        @DisplayName("입력 값에 대한 유효성 검증을 할 때,")
        class validate {

            @Nested
            @DisplayName("상품 고유 ID가")
            class productId {

                @Test
                @WithAuthUser
                @DisplayName("숫자 외의 값이 들어오면 예외가 발생합니다.")
                void invalidInput() throws Exception {
                    // given
                    final String productId = "가나디";

                    // when & then
                    mockMvc.perform(get("/api/products/{productId}", productId))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }

                @Test
                @WithAuthUser
                @DisplayName("1보다 작으면 예외가 발생합니다.")
                void isMin() throws Exception {
                    // given
                    final Long productId = 0L;

                    // when & then
                    mockMvc.perform(get("/api/products/{productId}", productId))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("상품 고유 ID는 0보다 큰 값이여야 합니다.")));
                }
            }
        }

        @Test
        @WithAuthUser
        @DisplayName("상품이 유효하지 않으면 예외가 발생합니다.")
        void notFound() throws Exception {
            // given
            final Long productId = 703L;

            given(productService.getProduct(productId))
                    .willThrow(new ShoppingBusinessException("상품이 존재하지 않습니다."));

            // when & then
            mockMvc.perform(get("/api/products/{productId}", productId))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("상품이 존재하지 않습니다."));
        }

        @Test
        @WithAuthUser
        @DisplayName("성공적으로 상품을 조회합니다.")
        void success() throws Exception {
            // given
            final Long productId = 703L;
            final String name = "장난감";
            final BigDecimal price = BigDecimal.valueOf(3000L);
            final String imageUrl = "http://image.com";

            final ProductResponse expectedResponse =
                    new ProductResponse(productId, name, price, imageUrl);

            given(productService.getProduct(productId)).willReturn(expectedResponse);

            // when
            final MvcResult mvcResult =
                    mockMvc.perform(get("/api/products/{productId}", productId))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();

            final ProductResponse response =
                    ConverterHelper.toDto(
                            mvcResult.getResponse().getContentAsString(), ProductResponse.class);

            // then
            assertSoftly(
                    it -> {
                        it.assertThat(response.productId()).isEqualTo(productId);
                        it.assertThat(response.name()).isEqualTo(name);
                        it.assertThat(response.price()).isEqualTo(price);
                        it.assertThat(response.imageUrl()).isEqualTo(imageUrl);
                    });
        }
    }
}
