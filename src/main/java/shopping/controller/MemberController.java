package shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shopping.controller.dto.MemberRequest;
import shopping.service.MemberService;

@RestController
public class MemberController {

    @Autowired
    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> register(@RequestBody MemberRequest request) {
        service.register(request.toMember());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
