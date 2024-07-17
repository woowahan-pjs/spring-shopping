package shopping.customer.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.customer.api.dto.CustomerSignInRequest;
import shopping.customer.api.dto.CustomerSignInResponse;

@RequestMapping("/api/customers/sign-in")
@RestController
public class CustomerSignInApi {

    @PostMapping
    public ResponseEntity<?> signIn(@RequestBody final CustomerSignInRequest request) {
        return ResponseEntity.ok(new CustomerSignInResponse("test"));
    }
}






