package shopping.member.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import shopping.auth.JwtProvider;
import shopping.common.exception.EmailAlreadyExistsException;
import shopping.common.exception.LoginFailedException;
import shopping.member.domain.Member;
import shopping.member.dto.LoginRequest;
import shopping.member.dto.RegisterRequest;
import shopping.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    void 회원가입한다() {
        //given
        String email = "new@test.com";
        String rawPassword = "123456";
        String encodedPassword = "encodedPassword";
        String token = "jwtToken";

        given(memberRepository.existsByEmail(email)).willReturn(false);
        given(passwordEncoder.encode(rawPassword)).willReturn(encodedPassword);
        given(jwtProvider.createToken(email)).willReturn(token);

        RegisterRequest request = new RegisterRequest(email, rawPassword);

        //when
        var response = memberService.register(request);

        //then
        then(memberRepository).should().save(any(Member.class));
    }

    @Test
    void 회원가입_중복_이메일이면_예외가_발생한다() {
        //given
        String email = "exists@test.com";
        given(memberRepository.existsByEmail(email)).willReturn(true);

        //when / then
        RegisterRequest request = new RegisterRequest(email, "123456");

        assertThrows(EmailAlreadyExistsException.class,
                () -> memberService.register(request));
    }

    @Test
    void 로그인한다() {
        //given
        String email = "login@test.com";
        String rawPassword = "123456";
        String encodedPassword = "******";
        String token = "jwtToken";

        Member member = Member.create(email, encodedPassword);

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);
        given(jwtProvider.createToken(email)).willReturn(token);

        LoginRequest request = new LoginRequest(email, rawPassword);

        //when
        var response = memberService.login(request);

        //then
        assertThat(response.getToken()).isEqualTo(token);
    }

    @Test
    void 로그인_회원_없으면_예외발생() {
        //given
        String email = "notfound@test.com";
        String password = "123456";

        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

        //when / then
        LoginRequest request = new LoginRequest(email, password);

        assertThrows(LoginFailedException.class,
                () -> memberService.login(request));
    }

    @Test
    void 로그인_비밀번호_틀리면_예외발생() {
        //given
        String email = "login@test.com";
        String rawPassword = "wrong";
        String encodedPassword = "######";

        Member member = Member.create(email, encodedPassword);

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(false);

        LoginRequest request = new LoginRequest(email, rawPassword);

        //when / then
        assertThrows(LoginFailedException.class,
                () -> memberService.login(request));
    }
}
