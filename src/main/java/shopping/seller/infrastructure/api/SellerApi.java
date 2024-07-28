package shopping.seller.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.common.auth.Authorization;
import shopping.common.auth.AuthorizationType;
import shopping.common.auth.AuthorizationUser;
import shopping.seller.application.SellerSignInUseCase;
import shopping.seller.application.SellerSignOutUseCase;
import shopping.seller.application.SellerSignUpUseCase;
import shopping.seller.infrastructure.api.dto.SellerSignInHttpRequest;
import shopping.seller.infrastructure.api.dto.SellerSignInHttpResponse;
import shopping.seller.infrastructure.api.dto.SellerSignUpHttpRequest;

import java.net.URI;

@RequestMapping("/api/sellers")
@RestController
public class SellerApi {
    private final SellerSignUpUseCase sellerSignUpUseCase;
    private final SellerSignInUseCase sellerSignInUseCase;
    private final SellerSignOutUseCase sellerSignOutUseCase;

    public SellerApi(final SellerSignUpUseCase sellerSignUpUseCase, final SellerSignInUseCase sellerSignInUseCase, final SellerSignOutUseCase sellerSignOutUseCase) {
        this.sellerSignUpUseCase = sellerSignUpUseCase;
        this.sellerSignInUseCase = sellerSignInUseCase;
        this.sellerSignOutUseCase = sellerSignOutUseCase;
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

    @PostMapping("/sign-out")
    public ResponseEntity<SellerSignInHttpResponse> signOut(
            @Authorization({AuthorizationType.SELLER}) final AuthorizationUser authorizationUser
    ) {
        sellerSignOutUseCase.signOut(authorizationUser.userId());
        return ResponseEntity.ok().build();
    }
}
