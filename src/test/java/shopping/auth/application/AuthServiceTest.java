package shopping.auth.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.auth.dto.TokenRequest;
import shopping.auth.dto.TokenResponse;
import shopping.auth.infrastructure.JwtTokenProvider;
import shopping.constant.enums.YesNo;
import shopping.member.application.MemberService;
import shopping.member.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    public static final String EMAIL = "yalmung@email.com";
    public static final String PASSWORD = "1234";
    public static final String ENCRYPT_PASSWORD = "$2a$10$NYYTB/3qW.8QJT9x6ZrjHO5tkmGjDagnKluJSwvSyEtPgN5JtcudW";

    private AuthService authService;

    @Mock
    private MemberService memberService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;


    @BeforeEach
    void setUp() {
        authService = new AuthService(jwtTokenProvider, memberService);
    }

    @DisplayName("등록된 회원정보와 유효한 토큰으로 로그인을 한다.")
    @Test
    void login() {
        when(memberService.findMemberByEmailAndDelYn(anyString(), eq(YesNo.N)))
                .thenReturn(new Member(EMAIL, ENCRYPT_PASSWORD));
        when(jwtTokenProvider.createToken(anyString())).thenReturn("TOKEN");

        TokenResponse token = authService.login(new TokenRequest(EMAIL, PASSWORD));

        assertThat(token.getAccessToken()).isNotBlank();
    }
}
