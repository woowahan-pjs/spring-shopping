package shopping.customer.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.customer.api.dto.CustomerSignUpRequest;
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
    public ResponseEntity<?> signUp(@RequestBody final CustomerSignUpRequest request) {
        customerSignUpUseCase.signUp(request.toCommand());
        return ResponseEntity.created(URI.create("")).build();
    }
}
