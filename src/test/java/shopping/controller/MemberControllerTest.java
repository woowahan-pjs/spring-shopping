package shopping.controller;

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
import shopping.controller.dto.MemberRequest;
import shopping.domain.Member;
import shopping.service.MemberService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private MemberService service;

    public Member createMember() {
        return new Member("test@gmail.com", "password");
    }

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
}