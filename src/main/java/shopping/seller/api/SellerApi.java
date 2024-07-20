package shopping.seller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.seller.api.dto.SellerSignInHttpRequest;
import shopping.seller.api.dto.SellerSignInHttpResponse;
import shopping.seller.api.dto.SellerSignUpHttpRequest;
import shopping.seller.application.SellerSignInUseCase;
import shopping.seller.application.SellerSignUpUseCase;

import java.net.URI;

@RequestMapping("/api/sellers")
@RestController
public class SellerApi {
    private final SellerSignUpUseCase sellerSignUpUseCase;
    private final SellerSignInUseCase sellerSignInUseCase;

    public SellerApi(final SellerSignUpUseCase sellerSignUpUseCase, final SellerSignInUseCase sellerSignInUseCase) {
        this.sellerSignUpUseCase = sellerSignUpUseCase;
        this.sellerSignInUseCase = sellerSignInUseCase;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<URI> signUp(@RequestBody final SellerSignUpHttpRequest sellerSignUpHttpRequest) {
        sellerSignUpUseCase.signUp(sellerSignUpHttpRequest.toCommand());
        return ResponseEntity.created(URI.create("")).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SellerSignInHttpResponse> signIn(@RequestBody final SellerSignInHttpRequest sellerSignInHttpRequest) {
        final String accessToken = sellerSignInUseCase.signIn(sellerSignInHttpRequest.toCommand());
        return ResponseEntity.ok(new SellerSignInHttpResponse(accessToken));
    }
}
