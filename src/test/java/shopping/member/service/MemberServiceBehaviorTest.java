package shopping.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import shopping.auth.service.AuthService;
import shopping.auth.service.AuthTokens;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.member.adapter.in.api.LoginRequest;
import shopping.member.adapter.in.api.RegisterRequest;
import shopping.member.domain.Member;
import shopping.member.domain.MemberFixture;
import shopping.member.domain.MemberRepository;
import shopping.member.domain.MemberRole;
import shopping.member.domain.MemberStatus;

@ExtendWith(MockitoExtension.class)
@DisplayName("[회원] 회원 서비스 행위 단위 테스트")
class MemberServiceBehaviorTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository, authService, passwordEncoder);
    }

    @Test
    @DisplayName("이미 가입한 이메일로 회원 가입하면 중복을 알려준다")
    void registerThrowWhenEmailAlreadyExists() {
        // given
        RegisterRequest request = new RegisterRequest("user@example.com", "password123");
        when(memberRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(member(null, "user@example.com", "encoded", MemberStatus.ACTIVE, MemberRole.USER)));

        // when
        // then
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.MEMBER_EMAIL_DUPLICATE);
    }

    @Nested
    @DisplayName("로그인")
    class Login {
        @Test
        @DisplayName("이메일과 비밀번호가 맞으면 로그인 토큰을 발급한다")
        void success() {
            // given
            Member member = member(7L, "user@example.com", "encoded-password", MemberStatus.ACTIVE, MemberRole.USER);
            when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(member));
            when(passwordEncoder.matches("password123", "encoded-password")).thenReturn(true);
            when(authService.issueTokens(7L)).thenReturn(new AuthTokens("access-token", "refresh-token"));

            // when
            AuthTokens tokens = memberService.login(new LoginRequest("user@example.com", "password123"));

            // then
            assertThat(tokens).isEqualTo(new AuthTokens("access-token", "refresh-token"));
        }

        @Test
        @DisplayName("없는 이메일로 로그인하면 인증을 거부한다")
        void throwWhenEmailDoesNotExist() {
            // given
            when(memberRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

            // when
            // then
            assertThatThrownBy(() -> memberService.login(new LoginRequest("missing@example.com", "password123")))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.MEMBER_CREDENTIALS_INVALID);
        }

        @Test
        @DisplayName("비활성 회원은 로그인할 수 없다")
        void throwWhenMemberIsInactive() {
            // given
            Member member = member(null, "user@example.com", "encoded-password", MemberStatus.INACTIVE, MemberRole.USER);
            when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(member));

            // when
            // then
            assertThatThrownBy(() -> memberService.login(new LoginRequest("user@example.com", "password123")))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.MEMBER_CREDENTIALS_INVALID);
        }

        @Test
        @DisplayName("비밀번호가 다르면 로그인할 수 없다")
        void throwWhenPasswordIsInvalid() {
            // given
            Member member = member(null, "user@example.com", "encoded-password", MemberStatus.ACTIVE, MemberRole.USER);
            when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(member));
            when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

            // when
            // then
            assertThatThrownBy(() -> memberService.login(new LoginRequest("user@example.com", "wrong-password")))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.MEMBER_CREDENTIALS_INVALID);
        }
    }

    @Nested
    @DisplayName("판매자 권한 검사")
    class RequireActiveSeller {
        @Test
        @DisplayName("활성 판매자는 판매자 권한 검사를 통과한다")
        void success() {
            // given
            Member seller = member(null, "seller@example.com", "password", MemberStatus.ACTIVE, MemberRole.SELLER);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(seller));

            // when
            // then
            assertThatCode(() -> memberService.requireActiveSeller(1L))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("없는 회원은 판매자 권한 검사를 통과하지 못한다")
        void throwWhenMemberDoesNotExist() {
            // given
            when(memberRepository.findById(1L)).thenReturn(Optional.empty());

            // when
            // then
            assertThatThrownBy(() -> memberService.requireActiveSeller(1L))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.AUTHENTICATION_TOKEN_INVALID);
        }

        @Test
        @DisplayName("비활성 판매자는 판매자 권한 검사를 통과하지 못한다")
        void throwWhenSellerIsInactive() {
            // given
            Member seller = member(null, "seller@example.com", "password", MemberStatus.INACTIVE, MemberRole.SELLER);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(seller));

            // when
            // then
            assertThatThrownBy(() -> memberService.requireActiveSeller(1L))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.MEMBER_INACTIVE_FORBIDDEN);
        }

        @Test
        @DisplayName("활성 일반 회원은 판매자 권한 검사를 통과하지 못한다")
        void throwWhenMemberIsNotSeller() {
            // given
            Member user = member(null, "user@example.com", "password", MemberStatus.ACTIVE, MemberRole.USER);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(user));

            // when
            // then
            assertThatThrownBy(() -> memberService.requireActiveSeller(1L))
                    .isInstanceOf(ApiException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.MEMBER_SELLER_REQUIRED);
        }
    }

    @ParameterizedTest(name = "[{index}] 비밀번호={0}")
    @ValueSource(strings = {"password123", "Password123!", "1234567890"})
    @DisplayName("비밀번호 해시 전략이 바뀌어도 로그인 흐름을 유지한다")
    void loginSupportDifferentPasswordInputs(String password) {
        // given
        String encodedPassword = "encoded-" + password;
        Member member = member(8L, "user@example.com", encodedPassword, MemberStatus.ACTIVE, MemberRole.USER);
        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(authService.issueTokens(8L)).thenReturn(new AuthTokens("access-token", "refresh-token"));

        // when
        AuthTokens tokens = memberService.login(new LoginRequest("user@example.com", password));

        // then
        assertThat(tokens.accessToken()).isEqualTo("access-token");
    }

    private Member member(Long memberId, String email, String password, MemberStatus status, MemberRole role) {
        return MemberFixture.member(memberId, email, password, status, role);
    }
}
