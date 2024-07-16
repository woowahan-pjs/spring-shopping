package shopping.customer.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.customer.api.dto.CustomerRegistrationRequest;
import shopping.customer.application.CustomerRegistrationUseCase;

import java.net.URI;

@RequestMapping("/api/customers")
@RestController
public class CustomerRegistrationApi {

    private final CustomerRegistrationUseCase customerRegistrationUseCase;

    public CustomerRegistrationApi(final CustomerRegistrationUseCase customerRegistrationUseCase) {
        this.customerRegistrationUseCase = customerRegistrationUseCase;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody final CustomerRegistrationRequest request) {
        customerRegistrationUseCase.register(request.toCommand());
        return ResponseEntity.created(URI.create("")).build();
    }
}
