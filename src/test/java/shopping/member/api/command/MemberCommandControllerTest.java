package shopping.member.api.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shopping.member.api.dto.MemberLoginRequest;
import shopping.member.api.dto.MemberRegisterRequest;
import shopping.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberCommandController memberCommandController;

    @Test
    @DisplayName("회원가입 성공 시 201과 토큰을 반환한다")
    void test01() throws Exception {
        // given
        MemberRegisterRequest request = new MemberRegisterRequest("user@example.com", "password123");

        // when
        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty());

        // then
        assertThat(memberCommandController).isNotNull();
        assertThat(memberRepository.existsByEmail("user@example.com")).isTrue();
    }

    @Test
    @DisplayName("중복 이메일로 회원가입 시 400을 반환한다")
    void test02() throws Exception {
        // given
        MemberRegisterRequest request = new MemberRegisterRequest("user@example.com", "password123");
        register(request);

        // when & then
        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 사용 중인 이메일입니다."));
    }

    @Test
    @DisplayName("로그인 성공 시 200과 토큰을 반환한다")
    void test03() throws Exception {
        // given
        register(new MemberRegisterRequest("user@example.com", "password123"));
        MemberLoginRequest request = new MemberLoginRequest("user@example.com", "password123");

        // when & then
        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시 400을 반환한다")
    void test04() throws Exception {
        // given
        MemberLoginRequest request = new MemberLoginRequest("notfound@example.com", "password123");

        // when & then
        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("존재하지 않는 이메일입니다."));
    }

    @Test
    @DisplayName("비밀번호 불일치로 로그인 시 400을 반환한다")
    void test05() throws Exception {
        // given
        register(new MemberRegisterRequest("user@example.com", "password123"));
        MemberLoginRequest request = new MemberLoginRequest("user@example.com", "wrongpassword");

        // when & then
        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }

    private void register(MemberRegisterRequest request) throws Exception {
        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
    }
}
