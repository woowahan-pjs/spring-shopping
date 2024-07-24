package shopping.member.presentation

import io.kotest.core.annotation.DisplayName
import io.mockk.every
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import shopping.member.fixture.MemberFixture
import shopping.support.KotestControllerTestSupport

@DisplayName("MemberApi 테스트")
class MemberApiTest : KotestControllerTestSupport() {
    init {
        Given("회원 가입 요청이 왔을 때") {

            When("정상 적인 이메일, 비밀번호를 받았다면") {
                every { memberCommandService.createMember(any()) } returns 1L
                every { memberQueryService.findById(any()) } returns MemberFixture.`고객 1`.`회원 엔티티 생성`()

                val response =
                    mockMvc.post("/api/members") {
                        content = objectMapper.writeValueAsBytes(MemberFixture.`고객 1`.`회원 가입 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("201 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isCreated() }
                    }
                }

                Then("가입 된 회원의 id, email 을 반환 한다") {
                    response.andExpectAll {
                        jsonPath("$.data") { isNotEmpty() }
                        jsonPath("$.data.id") { isNumber() }
                        jsonPath("$.data.email") { isString() }
                    }
                }
            }

            When("이메일이 공백인 경우") {
                val response =
                    mockMvc.post("/api/members") {
                        content = objectMapper.writeValueAsBytes(MemberFixture.`이메일 공백 회원`.`회원 가입 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("이메일 검증 실패 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("이메일은 필수로 입력하셔야 합니다.")
                }
            }

            When("이메일이 null 인 경우") {
                val response =
                    mockMvc.post("/api/members") {
                        content = objectMapper.writeValueAsBytes(MemberFixture.`이메일 NULL 회원`.`회원 가입 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("이메일 검증 실패 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("이메일은 필수로 입력하셔야 합니다.")
                }
            }

            When("이메일이 형식이 올바르지 않은 경우") {
                val response =
                    mockMvc.post("/api/members") {
                        content = objectMapper.writeValueAsBytes(MemberFixture.`이메일 비정상 회원`.`회원 가입 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("이메일 검증 실패 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("이메일 형식이 올바르지 않습니다.")
                }
            }

            When("비밀번호가 공백인 경우") {
                val response =
                    mockMvc.post("/api/members") {
                        content = objectMapper.writeValueAsBytes(MemberFixture.`비밀번호 공백 회원`.`회원 가입 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("비밀번호 검증 실패 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("비밀번호는 필수로 입력하셔야 합니다.")
                }
            }

            When("비밀번호가 null 인 경우") {
                val response =
                    mockMvc.post("/api/members") {
                        content = objectMapper.writeValueAsBytes(MemberFixture.`비밀번호 NULL 회원`.`회원 가입 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("비밀번호 검증 실패 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("비밀번호는 필수로 입력하셔야 합니다.")
                }
            }
        }
    }
}
