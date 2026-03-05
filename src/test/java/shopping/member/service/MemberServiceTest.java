package shopping.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import shopping.auth.application.AuthService;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.member.api.RegisterRequest;
import shopping.member.api.TokenResponse;
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

    @Captor
    private ArgumentCaptor<Member> memberCaptor;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository, authService);
    }

    @Test
    void registerSaveWithDefaultRoleAndStatus() {
        RegisterRequest request = new RegisterRequest("user@example.com", "password123");
        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member member = invocation.getArgument(0);
            setId(member, 1L);
            return member;
        });
        when(authService.issueToken(1L)).thenReturn("issued-token");

        TokenResponse response = memberService.register(request);

        assertThat(response.token()).isEqualTo("issued-token");
        verify(memberRepository).save(memberCaptor.capture());
        Member savedMember = memberCaptor.getValue();
        assertThat(savedMember.getRole()).isEqualTo(MemberRole.USER);
        assertThat(savedMember.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void registerThrowDuplicateWhenSaveRaceOccurs() {
        RegisterRequest request = new RegisterRequest("user@example.com", "password123");
        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate email"));

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
