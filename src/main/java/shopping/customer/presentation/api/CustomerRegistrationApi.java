package shopping.customer.presentation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/api/customers")
@RestController
public class CustomerRegistrationApi {

    @PostMapping
    public ResponseEntity<?> register() {
        return ResponseEntity.created(URI.create("")).build();
    }
}
