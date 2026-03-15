package shopping.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shopping.auth.JwtTokenProvider;
import shopping.member.controller.dto.MemberRequest;
import shopping.member.domain.Member;
import shopping.member.service.MemberService;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shopping.member.domain.MemberFixture.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;


@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    JwtTokenProvider provider;

    @MockitoBean
    private MemberService service;

    public MemberRequest createMemberRequest() {
        return new MemberRequest("test@gmail.com", "password");
    }

    @Test
    @DisplayName("회원 가입한다.")
    void register() throws Exception {
        MemberRequest request = createMemberRequest();
        Member member = createMember();

        willDoNothing().given(service).register(member);

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

        willThrow(new IllegalArgumentException("이미 존재하는 이메일입니다.")).given(service).register(any());

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

        given(service.login(request.getEmail(), request.getPassword())).willReturn(member);
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

        willThrow(new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요.")).given(service).login(request.getEmail(), request.getPassword());

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 시 비밀번호가 다를 경우 예외 발생")
    void invalidPassword() throws Exception {
        MemberRequest request = new MemberRequest("test@gmail.com", "pass");

        willThrow(new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요.")).given(service).login(request.getEmail(), request.getPassword());

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

}