package shopping.wish.presentation

import io.mockk.every
import io.mockk.justRun
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import shopping.member.fixture.MemberFixture
import shopping.support.KotestControllerTestSupport
import shopping.wish.fixture.WishProductFixture

class WishListApiTest : KotestControllerTestSupport() {
    init {
        val auth = user(MemberFixture.`고객 1`.`회원 엔티티 생성`())

        Given("위시 리스트 상품 정보 등록 요청을 받았을 때") {
            When("상품 ID 가 올바르면") {
                justRun { wishListCommandService.addWishProduct(any(), any()) }

                val response = mockMvc.post("/api/wishes/products") {
                    with(auth)
                    content = objectMapper.writeValueAsBytes(WishProductFixture.`고객 1의 위시 리스트에 저장 된 상품 1`.`위시 리스트 상품 등록 REQUEST 생성`())
                    contentType = MediaType.APPLICATION_JSON
                }.andDo {
                    MockMvcResultHandlers.print()
                }

                Then("204 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isNoContent() }
                    }
                }

                Then("응답 바디는 공백 이다") {
                    response.andExpect {
                        content { string("") }
                    }
                }
            }

            When("상품 ID 가 null 인 경우") {
                val response = mockMvc.post("/api/wishes/products") {
                    with(auth)
                    content = objectMapper.writeValueAsBytes(WishProductFixture.`상품 정보 null`.`위시 리스트 상품 등록 REQUEST 생성`())
                    contentType = MediaType.APPLICATION_JSON
                }.andDo {
                    MockMvcResultHandlers.print()
                }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("상품 ID 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 ID 를 입력해주세요.")
                }
            }
        }

        Given("위시 리스트 상품 정보 제거 요청을 받았을 때") {
            When("상품 ID 가 올바르면") {
                justRun { wishListCommandService.deleteWishProduct(any(), any()) }

                val response = mockMvc.delete("/api/wishes/products") {
                    with(auth)
                    content = objectMapper.writeValueAsBytes(WishProductFixture.`고객 1의 위시 리스트에 저장 된 상품 1`.`위시 리스트 상품 제거 REQUEST 생성`())
                    contentType = MediaType.APPLICATION_JSON
                }.andDo {
                    MockMvcResultHandlers.print()
                }

                Then("204 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isNoContent() }
                    }
                }

                Then("응답 바디는 공백 이다") {
                    response.andExpect {
                        content { string("") }
                    }
                }
            }

            When("상품 ID 가 null 인 경우") {
                val response = mockMvc.delete("/api/wishes/products") {
                    with(auth)
                    content = objectMapper.writeValueAsBytes(WishProductFixture.`상품 정보 null`.`위시 리스트 상품 제거 REQUEST 생성`())
                    contentType = MediaType.APPLICATION_JSON
                }.andDo {
                    MockMvcResultHandlers.print()
                }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("상품 ID 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 ID 를 입력해주세요.")
                }
            }
        }

        Given("위시 리스트 목록 조회 요청을 받았을 때") {
            every { wishListQueryService.findWishList(any()) } returns emptySet()

            When("로그인 한 회원의 위시리스트를 반환한다") {
                val response = mockMvc.get("/api/wishes") {
                    with(auth)
                    contentType = MediaType.APPLICATION_JSON
                }.andDo {
                    MockMvcResultHandlers.print()
                }

                Then("200 상태 코드를 반환 받는다") {
                    response.andExpect {
                        status { isOk() }
                    }
                }

                Then("반환 받은 위시 리스트의 타입은 배열 이다") {
                    response.andExpectAll {
                        jsonPath("$.data") { isMap() }
                        jsonPath("$.data.wishList") { isArray() }
                    }
                }
            }
        }
    }
}
