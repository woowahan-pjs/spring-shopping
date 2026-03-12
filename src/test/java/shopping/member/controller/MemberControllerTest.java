package shopping.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import shopping.auth.JwtTokenProvider;
import shopping.member.controller.dto.MemberRequest;
import shopping.member.domain.Member;
import shopping.member.service.MemberService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static shopping.member.domain.MemberFixture.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

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
    void register() throws JsonProcessingException {
        MemberRequest request = createMemberRequest();
        Member member = createMember();

        willDoNothing().given(service).register(member);

        assertThat(mockMvcTester.post().uri("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("중복된 이메일이 존재하면 예외가 발생")
    void invalidDuplicateEmail() throws JsonProcessingException {
        MemberRequest request = createMemberRequest();

        willThrow(new IllegalArgumentException("이미 존재하는 이메일입니다.")).given(service).register(any());

        assertThat(mockMvcTester.post().uri("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("로그인한다.")
    void login() throws JsonProcessingException {
        Member member = createWithId(1L);
        MemberRequest request = createMemberRequest();
        String token = "mock-token";

        given(service.login(request.getEmail(), request.getPassword())).willReturn(member);
        given(provider.generate(member.getId())).willReturn(token);

        assertThat(mockMvcTester.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .hasPathSatisfying("$.token", t -> assertThat(t).isEqualTo(token));
    }

    @Test
    @DisplayName("로그인 시 이메일이 없을 경우 예외 발생")
    void notFoundEmail() throws JsonProcessingException {
        MemberRequest request = new MemberRequest("test", "password");

        willThrow(new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요.")).given(service).login(request.getEmail(), request.getPassword());

        assertThat(mockMvcTester.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("로그인 시 비밀번호가 다를 경우 예외 발생")
    void invalidPassword() throws JsonProcessingException {
        MemberRequest request = new MemberRequest("test@gmail.com", "pass");

        willThrow(new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요.")).given(service).login(request.getEmail(), request.getPassword());

        assertThat(mockMvcTester.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.BAD_REQUEST);
    }


}