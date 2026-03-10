package shopping.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shopping.common.exception.EmailAlreadyExistsException;
import shopping.common.exception.LoginFailedException;
import shopping.member.dto.AuthResponse;
import shopping.member.dto.LoginRequest;
import shopping.member.dto.RegisterRequest;
import shopping.member.service.MemberService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 회원가입한다() throws Exception {
        var request = new RegisterRequest("test@test.com", "12345678");
        var response = new AuthResponse("jwtToken");

        given(memberService.register(any(RegisterRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwtToken"));
    }

    @Test
    void 회원가입_중복_이메일이면_에러() throws Exception {
        var request = new RegisterRequest("existes@test.com", "12345678");

        given(memberService.register(any(RegisterRequest.class))).willThrow(new EmailAlreadyExistsException());

        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("EMAIL_ALREADY_EXISTS"));
    }

    @Test
    void 로그인한다() throws Exception {
        var request = new LoginRequest("test@test.com", "12345678");
        var response = new AuthResponse("jwtToken");

        given(memberService.login(any(LoginRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwtToken"));
    }

    @Test
    void 로그인_실패하면_에러() throws Exception {
        var request = new LoginRequest("wrong@test.com", "12345678");

        given(memberService.login(any(LoginRequest.class))).willThrow(new LoginFailedException());

        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("LOGIN_FAILED"));
    }
}
