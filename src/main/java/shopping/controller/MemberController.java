package shopping.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import shopping.entity.Member;
import shopping.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Member> join(@Valid @RequestBody Member member) {
        try {
            return ResponseEntity.ok(memberService.join(member));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
