package shopping.service;

import static shopping.util.CryptoUtil.encrypt;

import org.springframework.stereotype.Service;
import shopping.component.TokenStore;
import shopping.controller.dto.member.LoginRequestDto;
import shopping.controller.dto.member.SignUpMemberRequestDto;
import shopping.domain.Member;
import shopping.repository.MemberRepository;
import shopping.util.CryptoUtil;

@Service
public class MemberService {

	private final TokenStore tokenStore;
	private final MemberRepository memberRepository;

	public MemberService(TokenStore tokenStore, MemberRepository memberRepository) {
		this.tokenStore = tokenStore;
		this.memberRepository = memberRepository;
	}

	public void signUp(SignUpMemberRequestDto requestDto) {
		String encryptPassword = encrypt(requestDto.getPassword());
		Member member = Member.create(requestDto.getEmail(), encryptPassword, requestDto.getName());
		memberRepository.save(member);
	}

	public String login(LoginRequestDto requestDto) {
		// TODO: DB에서 email/password 검증 후 memberId 조회
		Long memberId = 1L;
		return tokenStore.save(memberId);
	}
}
