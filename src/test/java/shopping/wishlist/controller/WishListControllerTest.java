package shopping.wishlist.controller;

import helper.ConverterHelper;
import helper.WithAuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
import shopping.infra.exception.ShoppingBusinessException;
import shopping.infra.security.TestSecurityConfig;
import shopping.product.domain.Price;
import shopping.wishlist.domain.WishListSaveStatus;
import shopping.wishlist.dto.WishListItemResponse;
import shopping.wishlist.dto.WishListItemSaveRequest;
import shopping.wishlist.dto.WishListItemSaveSummary;
import shopping.wishlist.dto.WishListResponse;
import shopping.wishlist.dto.WishListSaveRequest;
import shopping.wishlist.dto.WishListSaveSummary;
import shopping.wishlist.dto.WishListSaveSummaryFixture;
import shopping.wishlist.service.WishListSaveService;
import shopping.wishlist.service.WishListService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = WishListController.class)
class WishListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WishListService wishListService;

    @MockitoBean
    private WishListSaveService wishListSaveService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Nested
    @DisplayName("위시 리스트를 조회할 때,")
    class getWishList {

        @Test
        @WithAuthUser
        @DisplayName("성공적으로 조회합니다.")
        void success() throws Exception {
            // given
            final Long userId = 1L;

            final Long wishId = 1L;
            final Long productId = 3L;
            final String name = "AAAAA";
            final Price price = Price.create(3000L);
            final String imageUrl = "http://com";

            final WishListResponse expectedResponse = new WishListResponse(userId, List.of(
                    new WishListItemResponse(wishId, productId, name, price, imageUrl)));
            given(wishListService.getWishList(userId)).willReturn(expectedResponse);

            // when
            final MvcResult mvcResult = mockMvc.perform(get("/api/wishes"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            final WishListResponse response = ConverterHelper.toDto(mvcResult.getResponse().getContentAsString(), WishListResponse.class);

            // then
            assertSoftly(it -> {
                it.assertThat(response.userId()).isEqualTo(userId);
                it.assertThat(response.items())
                    .first()
                    .satisfies(item -> assertSoftly(softAssertions -> {
                        softAssertions.assertThat(item.wishId()).isEqualTo(wishId);
                        softAssertions.assertThat(item.name()).isEqualTo(name);
                        softAssertions.assertThat(item.price()).isEqualTo(price);
                        softAssertions.assertThat(item.imageUrl()).isEqualTo(imageUrl);
                    }));
            });
        }

        @Test
        @WithAuthUser
        @DisplayName("위시 리스트가 없을 경우 빈 값을 응답합니다.")
        void isNull() throws Exception {
            // given
            final Long userId = 1L;
            given(wishListService.getWishList(userId)).willReturn(WishListResponse.from(userId, null));

            // when
            final MvcResult mvcResult = mockMvc.perform(get("/api/wishes"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            // then
            assertThat(mvcResult.getResponse().getContentAsString()).isBlank();
        }
    }

    @Nested
    @DisplayName("위시 리스트 항목을 저장할 때,")
    class registerWishList {

        @Nested
        @DisplayName("입력 값에 대한 유효성 검증을 할 때,")
        class validate {

            @Nested
            @DisplayName("위시 리스트 항목이")
            class items {

                @Test
                @WithAuthUser
                @DisplayName("1건 미만의 경우 예외가 발생합니다.")
                void isMin() throws Exception {
                    // given
                    final String requestBody = """
                        {
                          "items": [ ]
                        }
                        """;

                    // when & then
                    mockMvc.perform(post("/api/wishes")
                                    .content(requestBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(jsonPath("$.parameters[*].message", hasItem("위시리스트 상품은 1개 이상이어야 합니다.")));
                }

                @Test
                @WithAuthUser
                @DisplayName("10건 초과일 경우 예외가 발생합니다.")
                void isMax() throws Exception {
                    // given
                    final String requestBody = """
                        {
                           "items": [
                             {"productId": 701},
                             {"productId": 702},
                             {"productId": 703},
                             {"productId": 704},
                             {"productId": 705},
                             {"productId": 706},
                             {"productId": 707},
                             {"productId": 708},
                             {"productId": 709},
                             {"productId": 710},
                             {"productId": 711}
                           ]
                        }
                        """;

                    // when & then
                    mockMvc.perform(post("/api/wishes")
                                    .content(requestBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(jsonPath("$.parameters[*].message", hasItem("위시리스트 상품은 최대 10개까지 등록할 수 있습니다.")));
                }

                @Test
                @WithAuthUser
                @DisplayName("제외하고 요청할 경우 예외가 발생합니다.")
                void isNull() throws Exception {
                    final String requestBody = """
                        { }
                        """;

                    // when & then
                    mockMvc.perform(post("/api/wishes")
                                    .content(requestBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(jsonPath("$.parameters[*].message", hasItem("위시리스트 상품은 1개 이상이어야 합니다.")));
                }

                @Test
                @WithAuthUser
                @DisplayName("상품 고유 ID 없이 요청할 경우 예외가 발생합니다.")
                void isEmpty() throws Exception {
                    // given
                    final String requestBody = """
                        {
                           "items": [
                             {"productId": ""}
                           ]
                        }
                        """;

                    // when & then
                    mockMvc.perform(post("/api/wishes")
                                    .content(requestBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(jsonPath("$.parameters[*].message", hasItem("상품 고유 ID는 필수입니다.")));
                }
            }
        }

        @Test
        @WithAuthUser
        @DisplayName("등록에 성공합니다.")
        void success() throws Exception {
            // given
            final String requestBody = """
                {
                   "items": [
                     {"productId": 1},
                     {"productId": 2}
                   ]
                }
                """;

            final Long userId = 1L;
            final WishListSaveRequest request = new WishListSaveRequest(List.of(new WishListItemSaveRequest(1L), new WishListItemSaveRequest(2L)));

            given(wishListSaveService.registerWishList(userId, request))
                    .willReturn(
                            WishListSaveSummaryFixture.fixture(2, 1, 1, List.of(
                                WishListItemSaveSummary.success(1L, 1L),
                                WishListItemSaveSummary.fail(2L, "실패"))
                        )
                    );

            // when
            final MvcResult mvcResult =  mockMvc.perform(post("/api/wishes")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            final WishListSaveSummary result = ConverterHelper.toDto(mvcResult.getResponse().getContentAsString(), WishListSaveSummary.class);

            // then
            assertSoftly(it -> {
                it.assertThat(result.getAllCount()).isEqualTo(2);
                it.assertThat(result.getSuccessCount()).isEqualTo(1);
                it.assertThat(result.getFailCount()).isEqualTo(1);
                it.assertThat(result.getDetails()).filteredOn(detail -> detail.productId().equals(1L))
                    .singleElement()
                    .satisfies(detail -> assertSoftly(assertions -> {
                        assertions.assertThat(detail.wishId()).isEqualTo(1L);
                        assertions.assertThat(detail.status()).isEqualTo(WishListSaveStatus.SUCCEEDED);
                    }));
                it.assertThat(result.getDetails()).filteredOn(detail -> detail.productId().equals(2L))
                    .singleElement()
                    .satisfies(detail -> assertSoftly(assertions -> {
                        assertions.assertThat(detail.wishId()).isNull();
                        assertions.assertThat(detail.status()).isEqualTo(WishListSaveStatus.FAILED);
                        assertions.assertThat(detail.message()).isEqualTo("실패");
                    }));
            });
        }
    }

    @Nested
    @DisplayName("위시 리스트 항목을 삭제할 때,")
    class removeWishList {

        @Test
        @WithAuthUser
        @DisplayName("wishId는 1보다 크지 않으면 예외가 발생합니다.")
        void isMin() throws Exception {
            // given
            final Long wishId = 0L;

            mockMvc.perform(delete("/api/wishes/{wishId}", wishId))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(jsonPath("$.parameters[*].message", hasItem("위시 리스트 항목 고유 ID는 0보다 큰 값이여야 합니다.")));
        }

        @Test
        @WithAuthUser
        @DisplayName("등록된 항목이 없을 경우 예외가 발생합니다.")
        void invalid() throws Exception {
            // given
            final Long userId = 1L;
            final Long wishId = 33L;

            willThrow(new ShoppingBusinessException("등록된 위시 리스트가 존재하지 않습니다."))
                    .given(wishListService).removeWishList(userId, wishId);

            // when & then
            mockMvc.perform(delete("/api/wishes/{wishId}", wishId))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("등록된 위시 리스트가 존재하지 않습니다."));
        }

        @Test
        @WithAuthUser
        @DisplayName("성공적으로 삭제합니다.")
        void success() throws Exception {
            // given
            final Long userId = 1L;
            final Long wishId = 33L;

            willDoNothing().given(wishListService).removeWishList(userId, wishId);

            // when & then
            mockMvc.perform(delete("/api/wishes/{wishId}", wishId))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }
}
