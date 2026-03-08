package shopping.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static shopping.util.CryptoUtil.encrypt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shopping.controller.dto.member.LoginRequestDto;
import shopping.controller.dto.member.SignUpMemberRequestDto;
import shopping.exception.NotFoundException;
import shopping.exception.NotValidException;
import shopping.repository.MemberRepository;

@SpringBootTest
class MemberServiceIntegrationTest {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	void tearDown() {
		memberRepository.deleteAll();
	}

	@Test
	void signUp_회원가입_성공() {
		SignUpMemberRequestDto request = new SignUpMemberRequestDto("테스터", "test@test.com", "password1234");

		memberService.signUp(request);

		assertThat(memberRepository.findByEmail("test@test.com")).isPresent();
	}

	@Test
	void login_로그인_성공_토큰반환() {
		memberService.signUp(new SignUpMemberRequestDto("테스터", "test@test.com", "password1234"));
		LoginRequestDto loginRequest = new LoginRequestDto("test@test.com", encrypt("password1234"));

		String token = memberService.login(loginRequest);

		assertThat(token).isNotBlank();
	}

	@Test
	void login_존재하지_않는_이메일_예외발생() {
		LoginRequestDto loginRequest = new LoginRequestDto("none@test.com", encrypt("password1234"));

		assertThatThrownBy(() -> memberService.login(loginRequest))
			.isInstanceOf(NotFoundException.class);
	}

	@Test
	void login_비밀번호_불일치_예외발생() {
		memberService.signUp(new SignUpMemberRequestDto("테스터", "test@test.com", "password1234"));
		LoginRequestDto loginRequest = new LoginRequestDto("test@test.com", encrypt("wrongPassword"));

		assertThatThrownBy(() -> memberService.login(loginRequest))
			.isInstanceOf(NotValidException.class);
	}
}