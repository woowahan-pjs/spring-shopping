package shopping.seller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.seller.api.dto.SellerSignUpHttpRequest;
import shopping.seller.application.SellerSignUpUseCase;

import java.net.URI;

@RequestMapping("/api/sellers")
@RestController
public class SellerApi {
    private final SellerSignUpUseCase sellerSignUpUseCase;

    public SellerApi(final SellerSignUpUseCase sellerSignUpUseCase) {
        this.sellerSignUpUseCase = sellerSignUpUseCase;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<URI> signUp(@RequestBody final SellerSignUpHttpRequest sellerSignUpHttpRequest) {
        sellerSignUpUseCase.signUp(sellerSignUpHttpRequest.toCommand());
        return ResponseEntity.created(URI.create("")).build();
    }
}
