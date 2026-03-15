package shopping.service;

import static shopping.util.CryptoUtil.encrypt;

import org.springframework.stereotype.Service;
import shopping.component.TokenStore;
import shopping.controller.dto.member.LoginRequestDto;
import shopping.controller.dto.member.SignUpMemberRequestDto;
import shopping.domain.Member;
import shopping.exception.CustomExceptionEnum;
import shopping.exception.NotFoundException;
import shopping.repository.MemberRepository;

@Service
public class MemberService {

	private final TokenStore tokenStore;
	private final MemberRepository memberRepository;

	public MemberService(TokenStore tokenStore, MemberRepository memberRepository) {
		this.tokenStore = tokenStore;
		this.memberRepository = memberRepository;
	}

	public void signUp(SignUpMemberRequestDto requestDto) {
		String encryptPassword = encrypt(requestDto.getPassword(), requestDto.getEmail());
		Member member = Member.create(requestDto.getEmail(), encryptPassword, requestDto.getName());
		memberRepository.save(member);
	}

	public String login(LoginRequestDto requestDto) {
		Member member = memberRepository.findByEmail(requestDto.getEmail())
			.orElseThrow(() -> new NotFoundException(CustomExceptionEnum.NOT_EXIST_MEMBER));
		member.validPassword(requestDto.getEncryptPassword());
		Long memberId = member.getId();
		return tokenStore.save(memberId);
	}
}
