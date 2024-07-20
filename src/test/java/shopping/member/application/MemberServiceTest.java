package shopping.member.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.member.application.dto.MemberRequest;
import shopping.member.application.dto.MemberResponse;
import shopping.member.domain.Member;
import shopping.member.exception.InvalidMemberException;
import shopping.member.exception.MemberNotFoundException;
import shopping.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemberServiceTest {

    private MemberService memberService;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        memberService = new MemberService(memberRepository);
    }

    @Test
    @DisplayName("새로운 회원을 저장할 수 있다")
    void saveMember() {
        final MemberRequest request = new MemberRequest("test@test.com", "password");
        final Member member = new Member(request.getEmail(), request.getPassword());

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        final MemberResponse response = memberService.save(request);

        assertThat(response.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("중복된 email 로 회원을 저장하려고 하면 예외가 발생한다")
    void saveMemberWithDuplicateEmail() {
        final MemberRequest request = new MemberRequest("test@test.com", "password");
        final Member member = new Member(request.getEmail(), request.getPassword());

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.save(request))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage("중복된 email 입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID로 회원을 삭제하려고 하면 예외가 발생한다")
    void deleteNonExistentMember() {
        final Long memberId = 1L;

        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.delete(memberId))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }
}
