package shopping.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import shopping.member.controller.dto.MemberRequest;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRole;
import shopping.support.ControllerTestSupport;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shopping.member.domain.MemberFixture.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@DisplayName("회원 API")
class MemberControllerTest extends ControllerTestSupport {

    public MemberRequest createMemberRequest() {
        return new MemberRequest("test@gmail.com", "password");
    }

    @Test
    @DisplayName("회원 가입한다.")
    void register() throws Exception {
        MemberRequest request = createMemberRequest();
        Member member = createMember();

        willDoNothing().given(memberService).register(member);

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("members/register",
                        requestFields(
                                fieldWithPath("email").description("로그인 시 입력할 이메일"),
                                fieldWithPath("password").description("로그인 시 입력할 비밀번호")
                        )
                ));
    }

    @Test
    @DisplayName("중복된 이메일이 존재하면 예외가 발생")
    void invalidDuplicateEmail() throws Exception {
        MemberRequest request = createMemberRequest();

        willThrow(new IllegalArgumentException("이미 존재하는 이메일입니다.")).given(memberService).register(any());

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인한다.")
    void login() throws Exception {
        Member member = createWithId(1L);
        MemberRequest request = createMemberRequest();
        String token = "mock-token";

        given(memberService.login(request.email(), request.password())).willReturn(member);
        given(provider.generate(member)).willReturn(token);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk()
                        , jsonPath("$.accessToken").value(token))
                .andDo(document("members/login",
                        requestFields(
                                fieldWithPath("email").description("로그인 시 입력할 이메일"),
                                fieldWithPath("password").description("로그인 시 입력할 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("발급된 JWT 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("로그인 시 이메일이 없을 경우 예외 발생")
    void notFoundEmail() throws Exception {
        MemberRequest request = new MemberRequest("test", "password");

        willThrow(new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요.")).given(memberService).login(request.email(), request.password());

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 시 비밀번호가 다를 경우 예외 발생")
    void invalidPassword() throws Exception {
        MemberRequest request = new MemberRequest("test@gmail.com", "pass");

        willThrow(new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요.")).given(memberService).login(request.email(), request.password());

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("관리자를 추가한다.")
    void addAdmin() throws Exception {
        MemberRequest request = createMemberRequest();

        given(provider.extractRole(any())).willReturn(MemberRole.ADMIN);

        willDoNothing().given(memberService).adminRegister(any());

        mockMvc.perform(post("/admin/members")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("admin/members/register",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("email").description("로그인 시 입력할 이메일"),
                                fieldWithPath("password").description("로그인 시 입력할 비밀번호")
                        )
                ));
    }

    @Test
    @DisplayName("관리자가 아니면 관리자 추가에 실패")
    void addAdmin_forbidden() throws Exception {
        given(provider.extractRole(any())).willReturn(MemberRole.USER);

        mockMvc.perform(post("/admin/members")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMemberRequest())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("로그인 안하면 관리자 추가에 예외가 발생")
    void addAdmin_notLogin() throws Exception {
        mockMvc.perform(post("/admin/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMemberRequest())))
                .andExpect(status().isUnauthorized());
    }

}