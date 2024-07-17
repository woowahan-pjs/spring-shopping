package shopping.customer.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.customer.api.dto.CustomerSignInRequest;
import shopping.customer.api.dto.CustomerSignInResponse;
import shopping.customer.application.CustomerSignInUseCase;
import shopping.customer.domain.AccessToken;

@RequestMapping("/api/customers/sign-in")
@RestController
public class CustomerSignInApi {

    private final CustomerSignInUseCase customerSignInUseCase;

    public CustomerSignInApi(final CustomerSignInUseCase customerSignInUseCase) {
        this.customerSignInUseCase = customerSignInUseCase;
    }

    @PostMapping
    public ResponseEntity<CustomerSignInResponse> signIn(@RequestBody final CustomerSignInRequest request) {
        final AccessToken accessToken = customerSignInUseCase.signIn(request.toCommand());
        return ResponseEntity.ok(new CustomerSignInResponse(accessToken.accessToken()));
    }
}






