package shopping.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.member.application.MemberService;
import shopping.member.application.dto.MemberRequest;
import shopping.member.application.dto.MemberResponse;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody final MemberRequest request) {
        final MemberResponse member = memberService.save(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeMember(@PathVariable("id") final Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

