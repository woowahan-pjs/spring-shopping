package shopping.auth.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import shopping.auth.controller.dto.RefreshRequest;
import shopping.member.domain.Member;
import shopping.support.ControllerTestSupport;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shopping.member.domain.MemberFixture.createWithId;

@DisplayName("인가 API")
public class AuthControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("리프래쉬 토큰으로 재발급")
    void refresh() throws Exception {
        RefreshRequest request = new RefreshRequest("refresh-token");
        String newToken = "new-access-token";
        Member member = createWithId(1L);

        given(provider.extractMemberIdFromRefreshToken(any())).willReturn(1L);
        given(memberService.findMember(any())).willReturn(member);
        given(provider.generateAccessToken(member)).willReturn(newToken);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("auth/refresh",
                        requestFields(
                                fieldWithPath("refreshToken").description("리프레쉬 토큰")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("재발급된 JWT 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("만료된 리프래쉬 토큰")
    void invalidRefreshToken() throws Exception {
        RefreshRequest request = new RefreshRequest("refresh-token");

        willThrow(new IllegalArgumentException()).given(provider).extractMemberIdFromRefreshToken(request.refreshToken());

        mockMvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않은 회원")
    void notFoundMember() throws Exception {
        RefreshRequest request = new RefreshRequest("refresh-token");

        given(provider.extractMemberIdFromRefreshToken(any())).willReturn(999L);
        willThrow(new NoSuchElementException()).given(memberService).findMember(any());

        mockMvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
