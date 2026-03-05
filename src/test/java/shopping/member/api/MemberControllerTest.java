package shopping.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shopping.member.api.dto.MemberRequest;
import shopping.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 성공 시 201과 토큰을 반환한다")
    void test01() throws Exception {
        // given
        MemberRequest request = new MemberRequest("user@example.com", "password123");

        // when
        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty());

        // then
        assertThat(memberRepository.existsByEmail("user@example.com")).isTrue();
    }

    @Test
    @DisplayName("중복 이메일로 회원가입 시 400을 반환한다")
    void test02() throws Exception {
        // given
        MemberRequest request = new MemberRequest("user@example.com", "password123");
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
        MemberRequest request = new MemberRequest("user@example.com", "password123");
        register(request);

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
        MemberRequest request = new MemberRequest("notfound@example.com", "password123");

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
        register(new MemberRequest("user@example.com", "password123"));
        MemberRequest request = new MemberRequest("user@example.com", "wrongpassword");

        // when & then
        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }

    private void register(MemberRequest request) throws Exception {
        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
    }
}
