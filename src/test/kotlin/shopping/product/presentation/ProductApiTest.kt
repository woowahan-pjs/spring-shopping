package shopping.product.presentation

import io.kotest.datatest.withData
import io.mockk.every
import io.mockk.justRun
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import shopping.product.fixture.ProductFixture
import shopping.support.KotestControllerTestSupport

class ProductApiTest : KotestControllerTestSupport() {

    init {
        Given("상품 등록 요청이 왔을 때") {
            When("정상 적인 상품 정보를 받은 경우") {
                every { productCommandService.createProduct(ProductFixture.`상품 1`.`상품 생성 COMMAND 생성`()) } returns ProductFixture.`상품 1`.`상품 엔티티 생성`()

                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 1`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("201 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isCreated() }
                    }
                }

                Then("등록 된 상품의 ID 를 반환 한다") {
                    response.andExpect {
                        jsonPath("$.data.id") { isNumber() }
                    }
                }
            }

            When("상품 판매 가격이 null 인 경우") {
                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 판매 가격 NULL 상품`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }


                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("상품 가격 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 판매 가격을 입력해주세요.")
                }
            }

            When("상품 판매 가격이 최대 범위를 넘긴 경우") {
                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 판매 가격 최대값 초과 상품`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo { 
                        MockMvcResultHandlers.print()
                    }


                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 가격 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 판매 가격은 최대 999,999,999 입니다.")
                }
            }

            When("상품 판매 가격이 최소 범위 미만인 경우") {
                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 판매 가격 최소값 미만 상품`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON   
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }


                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        MockMvcResultMatchers.status().isBadRequest
                    }
                }

                Then("상품 가격 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 판매 가격은 음수일 수 없습니다.")
                }
            }

            When("상품 정가 값이 null 인 경우") {
                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 정가 NULL 상품`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo { 
                        MockMvcResultHandlers.print() 
                    }


                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("상품 가격 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 정가를 입력해주세요.")
                }
            }

            When("상품 정가 값이 최대 범위를 넘긴 경우") {
                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 정가 최대값 초과 상품`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo { 
                        MockMvcResultHandlers.print() 
                    }


                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {  
                        MockMvcResultMatchers.status().isBadRequest 
                    }
                }

                Then("상품 가격 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 정가는 최대 999,999,999 입니다.")
                }
            }

            When("상품 정가 값이 최소 범위 미만인 경우") {
                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 정가 최소값 미만 상품`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo { 
                        MockMvcResultHandlers.print() 
                    }


                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("상품 가격 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 정가는 음수일 수 없습니다.")
                }
            }

            When("상품 이름이 공백인 경우") {
                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 이름 공백 상품`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo { 
                        MockMvcResultHandlers.print() 
                    }


                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 이름을 입력해주세요.")
                }
            }

            When("상품 이름이 null 인 경우") {
                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 이름 공백 상품`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo { 
                        MockMvcResultHandlers.print() 
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 이름을 입력해주세요.")
                }
            }

            When("상품 이름의 길이가 최대 길이를 초과 한 경우") {
                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 이름 최대 길이 초과 상품`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 이름은 최대 15자 까지 입력할 수 있습니다.")
                }
            }

            withData(
                nameFn = { "허용 특수 문자 : $it" },
                listOf(
                    ProductFixture.`상품 이름 허용 특수 문자 상품1`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품2`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품3`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품4`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품5`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품6`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품7`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품8`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품9`,
                )
            ) {
                When("상품 이름에 허용된 특수 문자가 포함 된 경우") {
                    every { productCommandService.createProduct(it.`상품 생성 COMMAND 생성`()) } returns it.`상품 엔티티 생성`()

                    val response =
                        mockMvc.post("/api/products") {
                            content = objectMapper.writeValueAsBytes(it.`상품 등록 요청 DTO 생성`())
                            contentType = MediaType.APPLICATION_JSON
                        }.andDo {
                            MockMvcResultHandlers.print()
                        }

                    Then("201 상태 코드를 반환 한다") {
                        response.andExpect { 
                            status { isCreated() }
                        }
                    }

                    Then("등록 된 상품의 ID 를 반환 한다") {
                        response.andExpect { 
                            jsonPath("$.data.id") { isNumber() }
                        }
                    }
                }
            }

            When("상품 이름에 허용되지 않는 특수 문자가 포함 된 경우") {
                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 이름 허용되지 않은 특수 문자 상품`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 이름은 한글, 영어, 숫자, 허용 된 특수문자((, ), [, ], +, -, &, /, _) 사용 가능합니다.")
                }
            }

            When("상품 재고가 null 인 경우") {
                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 재고 NULL 상품`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 재고를 입력해주세요.")
                }
            }

            When("상품 재고가 음수인 경우") {
                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 재고 음수 상품`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 개수는 음수일 수 없습니다.")
                }
            }

            When("상품 이미지 경로가 null 인 경우") {
                val response =
                    mockMvc.post("/api/products") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 이미지 NULL 상품`.`상품 등록 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 이미지를 등록해주세요.")
                }
            }
        }

        Given("상품 수정 요청이 왔을 때") {
            When("정상 적인 상품 정보를 받은 경우") {
                justRun { productCommandService.modifyProduct(any(), ProductFixture.`상품 1`.`상품 수정 COMMAND 생성`()) }

                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 1`.`상품 수정 요청 DTO 생성`())
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

            When("상품 판매 가격이 null 인 경우") {
                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 판매 가격 NULL 상품`.`상품 수정 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 가격 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 판매 가격을 입력해주세요.")
                }
            }

            When("상품 판매 가격이 최대 범위를 넘긴 경우") {
                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 판매 가격 최대값 초과 상품`.`상품 수정 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 가격 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 판매 가격은 최대 999,999,999 입니다.")
                }
            }

            When("상품 판매 가격이 최소 범위 미만인 경우") {
                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 판매 가격 최소값 미만 상품`.`상품 수정 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }
                
                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 가격 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 판매 가격은 음수일 수 없습니다.")
                }
            }

            When("상품 정가 값이 null 인 경우") {
                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 정가 NULL 상품`.`상품 수정 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 가격 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 정가를 입력해주세요.")
                }
            }

            When("상품 정가 값이 최대 범위를 넘긴 경우") {
                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 정가 최대값 초과 상품`.`상품 수정 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 가격 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 정가는 최대 999,999,999 입니다.")
                }
            }

            When("상품 정가 값이 최소 범위 미만인 경우") {
                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 정가 최소값 미만 상품`.`상품 수정 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 가격 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 정가는 음수일 수 없습니다.")
                }
            }

            When("상품 이름이 공백인 경우") {
                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 이름 공백 상품`.`상품 수정 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 이름을 입력해주세요.")
                }
            }

            When("상품 이름이 null 인 경우") {
                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 이름 공백 상품`.`상품 수정 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 이름을 입력해주세요.")
                }
            }

            When("상품 이름의 길이가 최대 길이를 초과 한 경우") {
                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 이름 최대 길이 초과 상품`.`상품 수정 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect { 
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 이름은 최대 15자 까지 입력할 수 있습니다.")
                }
            }

            withData(
                nameFn = { "허용 특수 문자 : $it" },
                listOf(
                    ProductFixture.`상품 이름 허용 특수 문자 상품1`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품2`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품3`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품4`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품5`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품6`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품7`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품8`,
                    ProductFixture.`상품 이름 허용 특수 문자 상품9`,
                )
            ) {
                When("상품 이름에 허용된 특수 문자가 포함 된 경우") {
                    justRun { productCommandService.modifyProduct(any(), it.`상품 수정 COMMAND 생성`()) }

                    val response =
                        mockMvc.put("/api/products/1") {
                            content = objectMapper.writeValueAsBytes(it.`상품 수정 요청 DTO 생성`())
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
            }

            When("상품 이름에 허용되지 않는 특수 문자가 포함 된 경우") {
                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 이름 허용되지 않은 특수 문자 상품`.`상품 수정 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 이름은 한글, 영어, 숫자, 허용 된 특수문자((, ), [, ], +, -, &, /, _) 사용 가능합니다.")
                }
            }

            When("상품 재고가 null 인 경우") {
                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 재고 NULL 상품`.`상품 수정 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 재고를 입력해주세요.")
                }
            }

            When("상품 재고가 음수인 경우") {
                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 재고 음수 상품`.`상품 수정 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 개수는 음수일 수 없습니다.")
                }
            }

            When("상품 이미지 경로가 null 인 경우") {
                val response =
                    mockMvc.put("/api/products/1") {
                        content = objectMapper.writeValueAsBytes(ProductFixture.`상품 이미지 NULL 상품`.`상품 수정 요청 DTO 생성`())
                        contentType = MediaType.APPLICATION_JSON
                    }.andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("400 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isBadRequest() }
                    }
                }

                Then("상품 이름 에러 메시지를 반환 한다") {
                    response.isInvalidInputValueResponse("상품 이미지를 등록해주세요.")
                }
            }
        }

        Given("상품 삭제 요청이 왔을 때") {
            When("path param 으로 온 상품 ID 와 동일한 상품을 삭제 후") {
                justRun { productCommandService.deletedProduct(any()) }

                val response =
                    mockMvc.delete("/api/products/1")
                        .andDo {
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
        }

        Given("상품 상세 조회 요청이 왔을 때") {
            When("path param 으로 온 상품 ID 와 동일한 상품이 있는 경우") {
                every { productQueryService.findProductNotDeleted(any()) } returns ProductFixture.`상품 1`.`상품 엔티티 생성`()

                val response =
                    mockMvc.get("/api/products/1").andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("200 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isOk() }
                    }
                }

                Then("찾은 상품의 상세 정보를 반환 한다") {
                    response.andExpect {
                        jsonPath("$.data") { isMap() }
                        jsonPath("$.data.sellingPrice") { isNumber(); value(1000000.00) }
                        jsonPath("$.data.fixedPrice") { isNumber(); value(1000000.00) }
                        jsonPath("$.data.productName") { isString(); value("시원한 에어컨") }
                        jsonPath("$.data.productAmount") { isNumber(); value(100) }
                        jsonPath("$.data.productImage") { isString(); value("https://image.com") }
                        jsonPath("$.data.productDescription") { isString(); value("시원한 에어컨 이에용") }
                    }
                }
            }
        }

        Given("상품 목록 조회 요청이 왔을 때") {
            When("path param 으로 온 상품 ID 와 동일한 상품이 있는 경우") {
                every { productQueryService.findAllNotDeleted() } returns listOf(ProductFixture.`상품 1`.`상품 엔티티 생성`(), ProductFixture.`상품 2`.`상품 엔티티 생성`())

                val response =
                    mockMvc.get("/api/products").andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("200 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isOk() }
                    }
                }

                Then("찾은 상품 들의 정보를 반환 한다") {
                    response.andExpect {
                        jsonPath("$.data") { isMap() }
                        jsonPath("$.data.products") { isArray() }

                        jsonPath("$.data.products[0].sellingPrice") { isNumber(); value(1000000.00) }
                        jsonPath("$.data.products[0].fixedPrice") { isNumber(); value(1000000.00) }
                        jsonPath("$.data.products[0].productName") { isString(); value("시원한 에어컨") }
                        jsonPath("$.data.products[0].productAmount") { isNumber(); value(100) }
                        jsonPath("$.data.products[0].productImage") { isString(); value("https://image.com") }
                        jsonPath("$.data.products[0].productDescription") { isString(); value("시원한 에어컨 이에용") }

                        jsonPath("$.data.products[1].sellingPrice") { isNumber(); value(500000.00) }
                        jsonPath("$.data.products[1].fixedPrice") { isNumber(); value(1000000.00) }
                        jsonPath("$.data.products[1].productName") { isString(); value("시원한 에어컨 - 할인가") }
                        jsonPath("$.data.products[1].productAmount") { isNumber(); value(50) }
                        jsonPath("$.data.products[1].productImage") { isString(); value("https://image.com") }
                        jsonPath("$.data.products[1].productDescription") { isString(); value("시원한 에어컨 할인 제품 이에용") }
                    }
                }
            }

            When("path param 으로 온 상품 ID 와 동일한 상품이 없는 경우") {
                every { productQueryService.findAllNotDeleted() } returns listOf()

                val response =
                    mockMvc.get("/api/products").andDo {
                        MockMvcResultHandlers.print()
                    }

                Then("200 상태 코드를 반환 한다") {
                    response.andExpect {
                        status { isOk() }
                    }
                }

                Then("빈 배열을 반환 한다") {
                    response.andExpect {
                        jsonPath("$.data") { isMap() }
                        jsonPath("$.data.products") { isArray(); isEmpty() }
                    }
                }
            }
        }
    }

}
