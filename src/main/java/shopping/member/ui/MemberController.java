package shopping.member.ui;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.member.application.MemberService;
import shopping.member.dto.MemberRequest;
import shopping.member.dto.MemberResponse;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/members")
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @PostMapping()
    public ResponseEntity<MemberResponse.RegMemberResponse> createMember(@RequestBody @Valid MemberRequest.RegMember request) {
        request.validate();
        MemberResponse.RegMemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getMbrSn())).body(member);
    }

}
