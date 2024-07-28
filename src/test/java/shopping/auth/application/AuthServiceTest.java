package shopping.auth.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.auth.domain.LoginMember;
import shopping.auth.dto.TokenRequest;
import shopping.auth.dto.TokenResponse;
import shopping.auth.infrastructure.JwtTokenProvider;
import shopping.constant.enums.YesNo;
import shopping.exception.AuthorizationException;
import shopping.member.application.MemberService;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;

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
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;


    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository);
        authService = new AuthService(jwtTokenProvider, memberService);
    }

    @DisplayName("등록된 회원정보와 유효한 토큰으로 로그인을 한다.")
    @Test
    void login() {
        when(memberRepository.findByEmailAndDelYn(anyString(), eq(YesNo.N)))
                .thenReturn(new Member(EMAIL, ENCRYPT_PASSWORD));
        when(jwtTokenProvider.createToken(anyString())).thenReturn("TOKEN");

        TokenResponse token = authService.login(new TokenRequest(EMAIL, PASSWORD));

        assertThat(token.getAccessToken()).isNotBlank();
    }

    @DisplayName("등록되지 않은 회원정보로 로그인 시도 시 예외가 발생한다.")
    @Test
    void loginExceptionNotExists() {
        when(memberRepository.findByEmailAndDelYn(anyString(), eq(YesNo.N))).thenReturn(null);

        TokenRequest token = new TokenRequest(EMAIL, PASSWORD);
        Assertions.assertThatThrownBy(() -> authService.login(token))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageStartingWith("해당 회원이 존재하지 않습니다.");
    }

    @DisplayName("토큰이 유효하지 않으면 예외를 발생한다.")
    @Test
    void findMemberByInvalidAccessTokenException() {
        Assertions.assertThatThrownBy(() -> authService.findMemberByToken("invalid access token"))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageStartingWith("로그인이 필요한 서비스 입니다.");
    }

    @DisplayName("잘못된 비밀번호로 로그인 시도 시 예외가 발생한다.")
    @Test
    void loginExceptionByWrongPassword() {
        when(memberRepository.findByEmailAndDelYn(anyString(), eq(YesNo.N))).thenReturn(new Member(EMAIL, PASSWORD));

        TokenRequest token = new TokenRequest(EMAIL, "wrong password");
        Assertions.assertThatThrownBy(() -> authService.login(token))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageStartingWith("비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("토큰이 유효할 경우 회원정보를 반환한다.")
    @Test
    void findMemberByToken() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(EMAIL);
        when(memberRepository.findByEmailAndDelYn(anyString(), eq(YesNo.N))).thenReturn((new Member(EMAIL, PASSWORD)));

        LoginMember member = authService.findMemberByToken("token");

        Assertions.assertThat(member).isEqualTo(new LoginMember(member.getId(), EMAIL));
    }
}
