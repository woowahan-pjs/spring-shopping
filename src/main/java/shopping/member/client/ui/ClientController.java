package shopping.member.client.ui;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.common.auth.resolver.LoginClient;
import shopping.member.client.applicaton.ClientLoginService;
import shopping.member.client.applicaton.ClientWishService;
import shopping.member.client.applicaton.dto.ClientCreateRequest;
import shopping.member.client.applicaton.dto.ClientLoginRequest;
import shopping.member.client.applicaton.dto.ClientLoginResponse;
import shopping.member.client.applicaton.dto.WishProductResponse;
import shopping.member.client.domain.Client;

@RequestMapping("/api/client")
@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ClientLoginService clientLoginService;
    private final ClientWishService clientWishService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid final ClientCreateRequest request) {
        clientLoginService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<ClientLoginResponse> login(
            @RequestBody @Valid final ClientLoginRequest request) {
        final ClientLoginResponse response = clientLoginService.login(request);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/wish/{productId}")
    public ResponseEntity<Void> wish(@PathVariable final Long productId,
            @LoginClient Client client) {
        clientWishService.wish(productId, client);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/un-wish/{productId}")
    public ResponseEntity<Void> unWish(@PathVariable final Long productId,
            @LoginClient Client client) {
        clientWishService.unWish(productId, client);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/wish")
    public ResponseEntity<List<WishProductResponse>> findAll(@LoginClient Client client) {
        final List<WishProductResponse> responses = clientWishService.findAll(client);
        return ResponseEntity.ok().body(responses);
    }
}
