package shopping.auth.presentation

import io.kotest.core.annotation.DisplayName
import io.mockk.every
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import shopping.auth.fixture.TokenFixture
import shopping.member.fixture.MemberFixture
import shopping.support.KotestControllerTestSupport

@DisplayName("AuthenticationApi 테스트")
class AuthenticationApiTest : KotestControllerTestSupport() {
    init {
        Given("로그인 요청이 왔을 때") {

            When("올바른 이메일과 비밀번호로 요청이 오면") {
                every { authenticationCommandService.logIn(any()) } returns TokenFixture.`토큰 1`.`토큰 엔티티 생성`()

                val response =
                    mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/login")
                            .content(objectMapper.writeValueAsBytes(MemberFixture.`고객 1`.`로그인 요청 DTO 생성`()))
                            .contentType(MediaType.APPLICATION_JSON),
                    ).andDo(MockMvcResultHandlers.print())

                Then("200 상태 코드를 반환 한다") {
                    response.isStatusAs(HttpStatus.OK)
                }

                Then("토큰 쌍을 반환 한다") {
                    response.andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.data").isNotEmpty,
                        MockMvcResultMatchers.jsonPath("$.data.accessToken").isString,
                        MockMvcResultMatchers.jsonPath("$.data.refreshToken").isString,
                    )
                }
            }

            When("이메일이 공백인 경우") {
                val response =
                    mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/login")
                            .content(objectMapper.writeValueAsBytes(MemberFixture.`이메일 공백 회원`.`로그인 요청 DTO 생성`()))
                            .contentType(MediaType.APPLICATION_JSON),
                    ).andDo(MockMvcResultHandlers.print())

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect(MockMvcResultMatchers.status().isBadRequest)
                }

                Then("이메일 검증 실패 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("이메일은 필수로 입력하셔야 합니다.")
                }
            }

            When("이메일이 null 인 경우") {
                val response =
                    mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/login")
                            .content(objectMapper.writeValueAsBytes(MemberFixture.`이메일 NULL 회원`.`로그인 요청 DTO 생성`()))
                            .contentType(MediaType.APPLICATION_JSON),
                    ).andDo(MockMvcResultHandlers.print())

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect(MockMvcResultMatchers.status().isBadRequest)
                }

                Then("이메일 검증 실패 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("이메일은 필수로 입력하셔야 합니다.")
                }
            }

            When("이메일이 형식이 올바르지 않은 경우") {
                val response =
                    mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/login")
                            .content(objectMapper.writeValueAsBytes(MemberFixture.`이메일 비정상 회원`.`로그인 요청 DTO 생성`()))
                            .contentType(MediaType.APPLICATION_JSON),
                    ).andDo(MockMvcResultHandlers.print())

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect(MockMvcResultMatchers.status().isBadRequest)
                }

                Then("이메일 검증 실패 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("이메일 형식이 올바르지 않습니다.")
                }
            }

            When("비밀번호가 공백인 경우") {
                val response =
                    mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/login")
                            .content(objectMapper.writeValueAsBytes(MemberFixture.`비밀번호 공백 회원`.`로그인 요청 DTO 생성`()))
                            .contentType(MediaType.APPLICATION_JSON),
                    ).andDo(MockMvcResultHandlers.print())

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect(MockMvcResultMatchers.status().isBadRequest)
                }

                Then("비밀번호 검증 실패 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("비밀번호는 필수로 입력하셔야 합니다.")
                }
            }

            When("비밀번호가 null 인 경우") {
                val response =
                    mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/login")
                            .content(objectMapper.writeValueAsBytes(MemberFixture.`비밀번호 NULL 회원`.`로그인 요청 DTO 생성`()))
                            .contentType(MediaType.APPLICATION_JSON),
                    ).andDo(MockMvcResultHandlers.print())

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect(MockMvcResultMatchers.status().isBadRequest)
                }

                Then("비밀번호 검증 실패 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("비밀번호는 필수로 입력하셔야 합니다.")
                }
            }
        }

        Given("토큰 재발급 요청이 왔을 때") {

            When("토큰이 공백이 아니라면") {
                every { authenticationCommandService.refreshToken(any()) } returns TokenFixture.`토큰 2`.`토큰 엔티티 생성`()

                val response =
                    mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/refresh")
                            .content(objectMapper.writeValueAsBytes(TokenFixture.`토큰 1`.`토큰 재발급 요청 DTO 생성`()))
                            .contentType(MediaType.APPLICATION_JSON),
                    ).andDo(MockMvcResultHandlers.print())

                Then("200 상태 코드를 반환 한다") {
                    response.isStatusAs(HttpStatus.OK)
                }

                Then("토큰 쌍을 반환 한다") {
                    response.andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.data").isNotEmpty,
                        MockMvcResultMatchers.jsonPath("$.data.refreshToken").isString,
                    )
                }
            }

            When("토큰이 공백인 경우") {
                val response =
                    mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/refresh")
                            .content(objectMapper.writeValueAsBytes(TokenFixture.`REFRESH TOKEN 공백`.`토큰 재발급 요청 DTO 생성`()))
                            .contentType(MediaType.APPLICATION_JSON),
                    ).andDo(MockMvcResultHandlers.print())

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect(MockMvcResultMatchers.status().isBadRequest)
                }

                Then("이메일 검증 실패 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("토큰 값은 필수 입니다.")
                }
            }

            When("토큰이 null 인 경우") {
                val response =
                    mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/refresh")
                            .content(objectMapper.writeValueAsBytes(TokenFixture.`REFRESH TOKEN NULL`.`토큰 재발급 요청 DTO 생성`()))
                            .contentType(MediaType.APPLICATION_JSON),
                    ).andDo(MockMvcResultHandlers.print())

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect(MockMvcResultMatchers.status().isBadRequest)
                }

                Then("이메일 검증 실패 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("토큰 값은 필수 입니다.")
                }
            }
        }
    }
}
