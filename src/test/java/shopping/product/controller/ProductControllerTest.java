package shopping.product.controller;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import helper.ConverterHelper;
import helper.TestFixtureUtils;
import helper.WithAuthUser;
import shopping.auth.domain.Role;
import shopping.infra.exception.ShoppingBusinessException;
import shopping.infra.security.TestSecurityConfig;
import shopping.product.domain.Price;
import shopping.product.dto.ProductResponse;
import shopping.product.dto.ProductSaveRequest;
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
            final Price price = Price.create(3000L);
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

    @Nested
    @DisplayName("상품을 등록할 때,")
    class registerProduct {

        @Nested
        @DisplayName("입력 값에 대한 유효성 검증을 할 때,")
        class validate {

            @Nested
            @DisplayName("상품명이")
            class name {

                @ParameterizedTest
                @EmptySource
                @WithAuthUser(role = Role.ADMIN)
                @ValueSource(strings = {" "})
                @DisplayName("빈 값으로 입력되면 예외가 발생합니다.")
                void isNull(final String input) throws Exception {
                    // given
                    final String requestBody =
                            """
                            {
                              "name": "%s",
                              "price": "1650",
                              "imageUrl": "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg"
                            }
                            """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/products")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("상품명은 필수 입력 값입니다.")));
                }

                @Test
                @WithAuthUser(role = Role.ADMIN)
                @DisplayName("제외하고 요청시 예외가 발생합니다.")
                void isEmpty() throws Exception {
                    // given
                    final String requestBody =
                            """
                            {
                              "price": "1650",
                              "imageUrl": "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg"
                            }
                        """;

                    // when & then
                    mockMvc.perform(
                                    post("/api/products")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("상품명은 필수 입력 값입니다.")));
                }

                @Test
                @WithAuthUser(role = Role.ADMIN)
                @DisplayName("5자 미만인 경우 예외가 발생합니다.")
                void isMin() throws Exception {
                    // given
                    final String input = TestFixtureUtils.createStringWithLength(4);
                    final String requestBody =
                            """
                            {
                              "name": "%s",
                              "price": "1650",
                              "imageUrl": "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg"
                            }
                            """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/products")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("상품명은 최소 5자에서 최대 15자 까지 입력 가능합니다.")));
                }

                @Test
                @WithAuthUser(role = Role.ADMIN)
                @DisplayName("15자 초과인 경우 예외가 발생합니다.")
                void isMax() throws Exception {
                    // given
                    final String input = TestFixtureUtils.createStringWithLength(16);
                    final String requestBody =
                            """
                            {
                              "name": "%s",
                              "price": "1650",
                              "imageUrl": "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg"
                            }
                            """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/products")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("상품명은 최소 5자에서 최대 15자 까지 입력 가능합니다.")));
                }

                @ParameterizedTest
                @ValueSource(strings = {"!", "@", "#", "👓"})
                @WithAuthUser(role = Role.ADMIN)
                @DisplayName("허용하지 않는 특수문자가 포함된 경우 예외가 발생합니다.")
                void invalidPattern(final String input) throws Exception {
                    // given
                    final String requestBody =
                            """
                            {
                              "name": "%s",
                              "price": "1650",
                              "imageUrl": "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg"
                            }
                            """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/products")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("특수문자는 (, ), [, ], &, -, +, /, _`만 가능합니다.")));
                }
            }

            @Nested
            @DisplayName("가격이")
            class price {

                @Test
                @WithAuthUser(role = Role.ADMIN)
                @DisplayName("숫자 외의 값이 들어오면 예외가 발생합니다.")
                void invalidInput() throws Exception {
                    // given
                    final String input = "가나디";
                    final String requestBody =
                            """
                            {
                              "name": "나na3()[]&-+/_",
                              "price": "%s",
                              "imageUrl": "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg"
                            }
                            """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/products")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }

                @Test
                @WithAuthUser(role = Role.ADMIN)
                @DisplayName("1보다 작으면 예외가 발생합니다.")
                void isMin() throws Exception {
                    // given
                    final Long input = 0L;
                    final String requestBody =
                            """
                            {
                              "name": "나na3()[]&-+/_",
                              "price": "%s",
                              "imageUrl": "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg"
                            }
                            """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/products")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("가격은 1원 이상만 가능합니다.")));
                }

                @Test
                @WithAuthUser(role = Role.ADMIN)
                @DisplayName("9,999,999,999 초과인 경우 예외가 발생합니다.")
                void isMax() throws Exception {
                    // given
                    final Long input = 100_000_000_000L;
                    final String requestBody =
                            """
                            {
                              "name": "나na3()[]&-+/_",
                              "price": "%s",
                              "imageUrl": "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg"
                            }
                            """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/products")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("가격은 99999999999원 이하만 가능합니다.")));
                }
            }

            @Nested
            @DisplayName("이미지 URL이")
            class imageUrl {

                @ParameterizedTest
                @EmptySource
                @WithAuthUser(role = Role.ADMIN)
                @ValueSource(strings = {" "})
                @DisplayName("빈 값으로 입력되면 예외가 발생합니다.")
                void isNull(final String input) throws Exception {
                    // given
                    final String requestBody =
                            """
                            {
                              "name": "나na3()[]&-+/_",
                              "price": "1650",
                              "imageUrl": "%s"
                            }
                            """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/products")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("이미지 URL은 필수 입력 값입니다.")));
                }

                @Test
                @WithAuthUser(role = Role.ADMIN)
                @DisplayName("제외하고 요청시 예외가 발생합니다.")
                void isEmpty() throws Exception {
                    // given
                    final String requestBody =
                            """
                            {
                              "name": "나na3()[]&-+/_",
                              "price": "1650"
                            }
                            """;

                    // when & then
                    mockMvc.perform(
                                    post("/api/products")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("이미지 URL은 필수 입력 값입니다.")));
                }

                @Test
                @WithAuthUser(role = Role.ADMIN)
                @DisplayName("9자 미만인 경우 예외가 발생합니다.")
                void isMin() throws Exception {
                    // given
                    final String input = "https://";
                    final String requestBody =
                            """
                            {
                              "name": "나na3()[]&-+/_",
                              "price": "1650",
                              "imageUrl": "%s"
                            }
                            """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/products")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("이미지 URL은 최소 9자에서 최대 255자 까지 입력 가능합니다.")));
                }

                @Test
                @WithAuthUser(role = Role.ADMIN)
                @DisplayName("255자 초과인 경우 예외가 발생합니다.")
                void isMax() throws Exception {
                    // given
                    final String input = "http://" + TestFixtureUtils.createStringWithLength(249);
                    final String requestBody =
                            """
                            {
                              "name": "나na3()[]&-+/_",
                              "price": "1650",
                              "imageUrl": "%s"
                            }
                            """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/products")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("이미지 URL은 최소 9자에서 최대 255자 까지 입력 가능합니다.")));
                }

                @Test
                @WithAuthUser(role = Role.ADMIN)
                @DisplayName("http:// 혹은 https:// 로 시작하지 않으면 예외가 발생합니다.")
                void invalidUrl() throws Exception {
                    // given
                    final String input = TestFixtureUtils.createStringWithLength(20);
                    final String requestBody =
                            """
                                {
                                  "name": "나na3()[]&-+/_",
                                  "price": "1650",
                                  "imageUrl": "%s"
                                }
                                """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/products")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("http:// 또는 https:// 형식만 입력 가능합니다.")));
                }
            }
        }

        @Test
        @WithAuthUser(role = Role.ADMIN)
        @DisplayName("상품명에 비속어가 포함되어 있으면 예외가 발생합니다.")
        void profanityIsTrue() throws Exception {
            // given
            final String requestBody =
                    """
                    {
                      "name": "BAD WORD",
                      "price": "1650",
                      "imageUrl": "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg"
                    }
                    """;

            given(
                            productService.registerProduct(
                                    ConverterHelper.toDto(requestBody, ProductSaveRequest.class)))
                    .willThrow(new ShoppingBusinessException("상품명에 비속어가 포함되어 있습니다."));

            // when & then
            mockMvc.perform(
                            post("/api/products")
                                    .content(requestBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("상품명에 비속어가 포함되어 있습니다."));
        }

        @Test
        @WithAuthUser(role = Role.ADMIN)
        @DisplayName("성공적으로 등록합니다.")
        void success() throws Exception {
            // given
            final Long expectedProductId = 1L;
            final String expectedLocation = "http://localhost/api/products/" + expectedProductId;
            final String requestBody =
                    """
                    {
                      "name": "GOOD WORD",
                      "price": "1650",
                      "imageUrl": "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg"
                    }
                    """;

            given(
                            productService.registerProduct(
                                    ConverterHelper.toDto(requestBody, ProductSaveRequest.class)))
                    .willReturn(expectedProductId);

            // when & then
            mockMvc.perform(
                            post("/api/products")
                                    .content(requestBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.header().string("location", expectedLocation));
        }
    }
}
