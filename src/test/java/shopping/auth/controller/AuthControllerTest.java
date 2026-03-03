package shopping.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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
import shopping.auth.domain.DuplicateEmailException;
import shopping.auth.domain.InvalidUserException;
import shopping.auth.domain.Role;
import shopping.auth.dto.LoginRequest;
import shopping.auth.dto.LoginResponse;
import shopping.auth.dto.RegisterRequest;
import shopping.auth.dto.RegisterResponse;
import shopping.auth.service.AuthFacade;
import shopping.infra.security.TestSecurityConfig;

@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private AuthFacade authFacade;

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
    @DisplayName("회원 가입 할 때,")
    class register {

        @Nested
        @DisplayName("입력 값에 대한 유효성 검증을 할 때,")
        class validate {

            @Nested
            @DisplayName("이메일이")
            class email {

                @Test
                @DisplayName("이메일 형식에 일치하지 않은 경우 예외가 발생합니다.")
                void invalidEmail() throws Exception {
                    // given
                    final String input = "test#.com";
                    final String requestBody =
                            """
                        {
                          "email": "%s",
                          "password": "12345678",
                          "role": "ADMIN"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/register")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("유효한 이메일 형식이 아닙니다.")));
                }

                @ParameterizedTest
                @EmptySource
                @ValueSource(strings = {" "})
                @DisplayName("빈 값으로 입력되면 예외가 발생합니다.")
                void isNull(final String input) throws Exception {
                    // given
                    final String requestBody =
                            """
                        {
                          "email": "%s",
                          "password": "12345678",
                          "role": "ADMIN"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/register")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("이메일은 필수 입력 값입니다.")));
                }

                @Test
                @DisplayName("제외하고 요청시 예외가 발생합니다.")
                void isEmpty() throws Exception {
                    // given
                    final String requestBody =
                            """
                        {
                          "password": "12345678",
                          "role": "ADMIN"
                        }
                        """;

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/register")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("이메일은 필수 입력 값입니다.")));
                }

                @Test
                @DisplayName("5자 미만인 경우 예외가 발생합니다.")
                void isMin() throws Exception {
                    // given
                    final String input = TestFixtureUtils.createStringWithLength(4);
                    final String requestBody =
                            """
                        {
                          "email": "%s",
                          "password": "12345678",
                          "role": "ADMIN"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/register")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("이메일은 최소 5자에서 최대 320자 까지 입력 가능합니다.")));
                }

                @Test
                @DisplayName("320자 초과인 경우 예외가 발생합니다.")
                void isMax() throws Exception {
                    // given
                    final String input = TestFixtureUtils.createStringWithLength(321);
                    final String requestBody =
                            """
                        {
                          "email": "%s",
                          "password": "12345678",
                          "role": "ADMIN"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/register")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("이메일은 최소 5자에서 최대 320자 까지 입력 가능합니다.")));
                }
            }

            @Nested
            @DisplayName("비밀번호가")
            class password {

                @Test
                @DisplayName("8자 미만인 경우 예외가 발생합니다.")
                void isMin() throws Exception {
                    // given
                    final String input = TestFixtureUtils.createStringWithLength(7);
                    final String requestBody =
                            """
                        {
                          "email": "test@test.com",
                          "password": "%s",
                          "role": "ADMIN"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/register")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("비밀번호는 최소 8자에서 최대 100자 까지 입력 가능합니다.")));
                }

                @Test
                @DisplayName("100자 초과인 경우 예외가 발생합니다.")
                void isMax() throws Exception {
                    // given
                    final String input = TestFixtureUtils.createStringWithLength(101);
                    final String requestBody =
                            """
                        {
                          "email": "test@test.com",
                          "password": "%s",
                          "role": "ADMIN"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/register")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("비밀번호는 최소 8자에서 최대 100자 까지 입력 가능합니다.")));
                }

                @ParameterizedTest
                @EmptySource
                @ValueSource(strings = {" "})
                @DisplayName("빈 값으로 입력되면 예외가 발생합니다.")
                void isNull(final String input) throws Exception {
                    // given
                    final String requestBody =
                            """
                        {
                          "email": "test@test.com",
                          "password": "%s",
                          "role": "ADMIN"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/register")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("비밀번호는 필수 입력 값입니다.")));
                }

                @Test
                @DisplayName("제외하고 요청시 예외가 발생합니다.")
                void isEmpty() throws Exception {
                    // given
                    final String requestBody =
                            """
                        {
                          "email": "test@test.com",
                          "role": "ADMIN"
                        }
                        """;

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/register")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("비밀번호는 필수 입력 값입니다.")));
                }
            }

            @Nested
            @DisplayName("권한이")
            class role {

                @Test
                @DisplayName("유효하지 않은 경우, 예외가 발생합니다.")
                void isInvalid() throws Exception {
                    // given
                    final String input = "TESTER";
                    final String requestBody =
                            """
                        {
                          "email": "test@test.com",
                          "password": "12345678",
                          "role": "%s"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/register")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message", hasItem("권한은 필수 입력 값입니다.")));
                }

                @Test
                @DisplayName("제외하고 요청시 예외가 발생합니다.")
                void isEmpty() throws Exception {
                    // given
                    final String requestBody =
                            """
                        {
                          "email": "test@test.com",
                          "password": "12345678"
                        }
                        """;

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/register")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message", hasItem("권한은 필수 입력 값입니다.")));
                }
            }
        }

        @Test
        @DisplayName("중복 이메일인 경우, 예외가 발생합니다.")
        void duplicate() throws Exception {
            // given
            final String email = "test@test.com";
            final String password = "12345678";
            final Role role = Role.CUSTOMER;

            final String requestBody =
                    """
                {
                  "email": "%s",
                  "password": "%s",
                  "role": "%s"
                }
                """
                            .formatted(email, password, role.name());

            given(authFacade.register(eq(new RegisterRequest(email, password, role))))
                    .willThrow(new DuplicateEmailException());

            // when & then
            mockMvc.perform(
                            post("/api/members/register")
                                    .content(requestBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isConflict())
                    .andExpect(jsonPath("$.detail").value("이미 등록된 이메일입니다."));
        }

        @Test
        @DisplayName("성공적으로 회원 가입을 합니다.")
        void success() throws Exception {
            // given
            final String expectedResult = "EXPECTED-TOKEN";

            final String email = "test@test.com";
            final String password = "12345678";
            final Role role = Role.CUSTOMER;

            final String requestBody =
                    """
                {
                  "email": "%s",
                  "password": "%s",
                  "role": "%s"
                }
                """
                            .formatted(email, password, role.name());

            given(authFacade.register(eq(new RegisterRequest(email, password, role))))
                    .willReturn(RegisterResponse.of(expectedResult));

            // when
            final MvcResult mvcResult =
                    mockMvc.perform(
                                    post("/api/members/register")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();

            final RegisterResponse response =
                    ConverterHelper.toDto(
                            mvcResult.getResponse().getContentAsString(), RegisterResponse.class);

            // then
            assertThat(response.accessToken()).isEqualTo(expectedResult);
        }
    }

    @Nested
    @DisplayName("로그인을 할 때,")
    class login {

        @Nested
        @DisplayName("입력 값에 대한 유효성 검증을 할 때,")
        class validate {

            @Nested
            @DisplayName("이메일이")
            class email {

                @Test
                @DisplayName("이메일 형식에 일치하지 않은 경우 예외가 발생합니다.")
                void invalidEmail() throws Exception {
                    // given
                    final String input = "test#.com";
                    final String requestBody =
                            """
                        {
                          "email": "%s",
                          "password": "12345678"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/login")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("유효한 이메일 형식이 아닙니다.")));
                }

                @ParameterizedTest
                @EmptySource
                @ValueSource(strings = {" "})
                @DisplayName("빈 값으로 입력되면 예외가 발생합니다.")
                void isNull(final String input) throws Exception {
                    // given
                    final String requestBody =
                            """
                        {
                          "email": "%s",
                          "password": "12345678"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/login")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("이메일은 필수 입력 값입니다.")));
                }

                @Test
                @DisplayName("제외하고 요청시 예외가 발생합니다.")
                void isEmpty() throws Exception {
                    // given
                    final String requestBody =
                            """
                        {
                          "password": "12345678"
                        }
                        """;

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/login")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("이메일은 필수 입력 값입니다.")));
                }

                @Test
                @DisplayName("5자 미만인 경우 예외가 발생합니다.")
                void isMin() throws Exception {
                    // given
                    final String input = TestFixtureUtils.createStringWithLength(4);
                    final String requestBody =
                            """
                        {
                          "email": "%s",
                          "password": "12345678"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/login")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("이메일은 최소 5자에서 최대 320자 까지 입력 가능합니다.")));
                }

                @Test
                @DisplayName("320자 초과인 경우 예외가 발생합니다.")
                void isMax() throws Exception {
                    // given
                    final String input = TestFixtureUtils.createStringWithLength(321);
                    final String requestBody =
                            """
                        {
                          "email": "%s",
                          "password": "12345678"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/login")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("이메일은 최소 5자에서 최대 320자 까지 입력 가능합니다.")));
                }
            }

            @Nested
            @DisplayName("비밀번호가")
            class password {

                @Test
                @DisplayName("8자 미만인 경우 예외가 발생합니다.")
                void isMin() throws Exception {
                    // given
                    final String input = TestFixtureUtils.createStringWithLength(7);
                    final String requestBody =
                            """
                        {
                          "email": "test@test.com",
                          "password": "%s"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/login")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("비밀번호는 최소 8자에서 최대 100자 까지 입력 가능합니다.")));
                }

                @Test
                @DisplayName("100자 초과인 경우 예외가 발생합니다.")
                void isMax() throws Exception {
                    // given
                    final String input = TestFixtureUtils.createStringWithLength(101);
                    final String requestBody =
                            """
                        {
                          "email": "test@test.com",
                          "password": "%s"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/login")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("비밀번호는 최소 8자에서 최대 100자 까지 입력 가능합니다.")));
                }

                @ParameterizedTest
                @EmptySource
                @ValueSource(strings = {" "})
                @DisplayName("빈 값으로 입력되면 예외가 발생합니다.")
                void isNull(final String input) throws Exception {
                    // given
                    final String requestBody =
                            """
                        {
                          "email": "test@test.com",
                          "password": "%s"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/login")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("비밀번호는 필수 입력 값입니다.")));
                }

                @Test
                @DisplayName("제외하고 요청시 예외가 발생합니다.")
                void isEmpty() throws Exception {
                    // given
                    final String requestBody =
                            """
                        {
                          "email": "test@test.com"
                        }
                        """;

                    // when & then
                    mockMvc.perform(
                                    post("/api/members/login")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(
                                    jsonPath(
                                            "$.parameters[*].message",
                                            hasItem("비밀번호는 필수 입력 값입니다.")));
                }
            }
        }

        @Test
        @DisplayName("회원 정보가 없거나, 비밀번호가 일치하지 않는 경우 예외가 발생합니다.")
        void invalid() throws Exception {
            // given
            final String email = "test@test.com";
            final String password = "12345678";

            final String requestBody =
                    """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """
                            .formatted(email, password);

            given(authFacade.login(eq(new LoginRequest(email, password))))
                    .willThrow(new InvalidUserException());

            // when & then
            mockMvc.perform(
                            post("/api/members/login")
                                    .content(requestBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("이메일 또는 비밀번호를 확인해주세요."));
        }

        @Test
        @DisplayName("성공적으로 로그인을하여 accessToken을 발급 받습니다.")
        void success() throws Exception {
            // given
            final String email = "test@test.com";
            final String password = "12345678";

            final String requestBody =
                    """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """
                            .formatted(email, password);

            final String expectedToken = "EXPECTED-TOKEN";

            given(authFacade.login(eq(new LoginRequest(email, password))))
                    .willReturn(LoginResponse.of(expectedToken));

            // when
            final MvcResult mvcResult =
                    mockMvc.perform(
                                    post("/api/members/login")
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();

            final LoginResponse response =
                    ConverterHelper.toDto(
                            mvcResult.getResponse().getContentAsString(), LoginResponse.class);

            // then
            assertThat(response.accessToken()).isEqualTo(expectedToken);
        }
    }
}
