package shopping.member.ui;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<MemberResponse.MemberDetail> createMember(@RequestBody @Valid MemberRequest.RegMember request) {
        request.validate();
        MemberResponse.MemberDetail member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getMbrSn())).body(member);
    }

    @GetMapping("/{sn}")
    public ResponseEntity<MemberResponse.MemberDetail> findMemberBySn(@PathVariable Long sn) {
        return ResponseEntity.ok().body(memberService.findMemberDetailResponseBySn(sn));
    }

}
