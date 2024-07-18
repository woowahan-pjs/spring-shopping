package shopping.customer.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.customer.api.dto.CustomerSignUpHttpRequest;
import shopping.customer.application.CustomerSignUpUseCase;

import java.net.URI;

@RequestMapping("/api/customers/sign-up")
@RestController
public class CustomerSignUpApi {

    private final CustomerSignUpUseCase customerSignUpUseCase;

    public CustomerSignUpApi(final CustomerSignUpUseCase customerSignUpUseCase) {
        this.customerSignUpUseCase = customerSignUpUseCase;
    }

    @PostMapping
    public ResponseEntity<URI> signUp(@RequestBody final CustomerSignUpHttpRequest request) {
        customerSignUpUseCase.signUp(request.toCommand());
        return ResponseEntity.created(URI.create("")).build();
    }
}
