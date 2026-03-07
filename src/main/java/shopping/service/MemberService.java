package shopping.service;

import org.springframework.stereotype.Service;
import shopping.component.TokenStore;
import shopping.controller.dto.member.LoginRequestDto;
import shopping.controller.dto.member.SignUpMemberRequestDto;

@Service
public class MemberService {

	private final TokenStore tokenStore;

	public MemberService(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	public void signUp(SignUpMemberRequestDto requestDto) {

	}

	public String login(LoginRequestDto requestDto) {
		// TODO: DB에서 email/password 검증 후 memberId 조회
		Long memberId = 1L;
		return tokenStore.save(memberId);
	}
}
