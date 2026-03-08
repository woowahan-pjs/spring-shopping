package shopping.member.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import shopping.member.application.dto.MemberLoginRequest;
import shopping.member.application.dto.MemberSignUpRequest;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공 시 토큰을 반환한다")
    void register_success() throws Exception {
        MemberSignUpRequest request = new MemberSignUpRequest("test@test.com", "password123");

        MvcResult result = mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.token").isNotEmpty())
            .andReturn();
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입 시 409를 반환한다")
    void register_duplicateEmail() throws Exception {
        MemberSignUpRequest request = new MemberSignUpRequest("duplicate@test.com", "password123");

        mockMvc.perform(post("/api/members/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("이메일 형식이 올바르지 않으면 400을 반환한다")
    void register_invalidEmail() throws Exception {
        MemberSignUpRequest request = new MemberSignUpRequest("invalid-email", "password123");

        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 성공 시 토큰을 반환한다")
    void login_success() throws Exception {
        MemberSignUpRequest signUpRequest = new MemberSignUpRequest("login@test.com",
            "password123");
        mockMvc.perform(post("/api/members/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpRequest)));

        MemberLoginRequest loginRequest = new MemberLoginRequest("login@test.com", "password123");

        mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.token").isNotEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시 401을 반환한다")
    void login_notExistEmail() throws Exception {
        MemberLoginRequest request = new MemberLoginRequest("none@test.com", "password123");

        mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("비밀번호가 틀리면 401을 반환한다")
    void login_wrongPassword() throws Exception {
        MemberSignUpRequest signUpRequest = new MemberSignUpRequest("wrong@test.com",
            "password123");
        mockMvc.perform(post("/api/members/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpRequest)));

        MemberLoginRequest loginRequest = new MemberLoginRequest("wrong@test.com", "wrongpassword");

        mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized());
    }
}
