package shopping.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.controller.dto.member.LoginRequestDto;
import shopping.controller.dto.member.SignUpMemberRequestDto;
import shopping.service.MemberService;

@RestController
@RequestMapping("/api/member")
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/sign-up")
	public ResponseEntity<Object> signUp(SignUpMemberRequestDto requestDto) {
		memberService.signUp(requestDto);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginRequestDto requestDto) {
		String token = memberService.login(requestDto);
		return ResponseEntity.ok()
			.header("Authorization", token)
			.build();
	}
}
