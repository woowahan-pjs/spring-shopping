package shopping.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {

	@PostMapping("/sign-up")
	public void signUp() {

	}

	@PostMapping("/login")
	public void login() {

	}
}
