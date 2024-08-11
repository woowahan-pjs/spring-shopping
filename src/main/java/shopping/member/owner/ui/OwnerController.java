package shopping.member.owner.ui;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.member.owner.application.OwnerLoginService;
import shopping.member.owner.application.dto.OwnerCreateRequest;
import shopping.member.owner.application.dto.OwnerLoginRequest;
import shopping.member.owner.application.dto.OwnerLoginResponse;

@RequestMapping("/api/owner")
@RestController
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerLoginService ownerLoginService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid final OwnerCreateRequest request) {
        ownerLoginService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<OwnerLoginResponse> login(
            @RequestBody @Valid final OwnerLoginRequest request) {
        final OwnerLoginResponse response = ownerLoginService.login(request);
        return ResponseEntity.ok(response);
    }
}
