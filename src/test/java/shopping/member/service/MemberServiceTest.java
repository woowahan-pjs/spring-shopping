package shopping.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import shopping.auth.service.AuthService;
import shopping.auth.service.AuthTokens;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.member.adapter.in.api.RegisterRequest;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;
import shopping.member.domain.MemberRole;
import shopping.member.domain.MemberStatus;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<Member> memberCaptor;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository, authService, passwordEncoder);
    }

    @Test
    @DisplayName("회원을 가입하면 기본 역할과 상태로 저장하고 토큰을 발급한다")
    void registerSaveWithDefaultRoleAndStatus() {
        // given
        RegisterRequest request = new RegisterRequest("user@example.com", "password123");
        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member member = invocation.getArgument(0);
            setId(member, 1L);
            return member;
        });
        when(authService.issueTokens(1L)).thenReturn(new AuthTokens("issued-access-token", "issued-refresh-token"));

        // when
        AuthTokens response = memberService.register(request);

        // then
        assertThat(response.accessToken()).isEqualTo("issued-access-token");
        assertThat(response.refreshToken()).isEqualTo("issued-refresh-token");
        verify(memberRepository).save(memberCaptor.capture());
        Member savedMember = memberCaptor.getValue();
        assertThat(savedMember.getRole()).isEqualTo(MemberRole.USER);
        assertThat(savedMember.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(savedMember.getPassword()).isEqualTo("encoded-password");
    }

    @Test
    @DisplayName("회원 저장이 중복으로 실패하면 이메일 중복을 알려준다")
    void registerThrowDuplicateWhenSaveRaceOccurs() {
        // given
        RegisterRequest request = new RegisterRequest("user@example.com", "password123");
        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(memberRepository.save(any(Member.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate email"));

        // when
        // then
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.MEMBER_EMAIL_DUPLICATE);
    }

    private void setId(Member member, Long id) {
        try {
            Field idField = Member.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(member, id);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to set member id", exception);
        }
    }
}
