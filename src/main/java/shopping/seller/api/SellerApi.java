package shopping.seller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.seller.api.dto.SellerSignUpRequest;

import java.net.URI;

@RequestMapping("/api/sellers")
@RestController
public class SellerApi {

    @PostMapping("/sign-up")
    public ResponseEntity<URI> signUp(@RequestBody final SellerSignUpRequest sellerSignUpRequest) {
        return ResponseEntity.created(URI.create("")).build();
    }
}
