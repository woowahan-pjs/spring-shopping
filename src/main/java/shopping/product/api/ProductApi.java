package shopping.product.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.product.api.dto.ProductRegistrationHttpRequest;

import java.net.URI;

@RequestMapping("/internal-api/shops")
@RestController
public class ProductApi {

    @PostMapping("/{shopId}/products")
    public ResponseEntity<?> registerProduct(
            @PathVariable(name = "shopId") final long shopId,
            @RequestBody final ProductRegistrationHttpRequest request
    ) {
        return ResponseEntity.created(URI.create("")).build();
    }
}
