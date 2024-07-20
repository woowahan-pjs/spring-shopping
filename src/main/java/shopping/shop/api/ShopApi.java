package shopping.shop.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.shop.api.dto.ShopRegistrationRequest;

import java.net.URI;

@RequestMapping("/api/sellers/shops")
@RestController
public class ShopApi {

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody final ShopRegistrationRequest request) {
        return ResponseEntity.created(URI.create("")).build();
    }
}
