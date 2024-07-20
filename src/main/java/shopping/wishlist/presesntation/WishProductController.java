package shopping.wishlist.presesntation;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.auth.AuthenticationPrincipal;
import shopping.auth.UserDetails;
import shopping.wishlist.application.WishProductService;
import shopping.wishlist.domain.WishProduct;

@RequestMapping("/wish-products")
@RestController
public class WishProductController {
    private final WishProductService wishProductService;

    public WishProductController(WishProductService wishProductService) {
        this.wishProductService = wishProductService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ProductIdRequest productIdRequest) {
        wishProductService.add(userDetails.email(), productIdRequest.productId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<WishProductsResponse> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        List<WishProduct> wishProducts = wishProductService.getAll(userDetails.email());
        return ResponseEntity.ok(WishProductsResponse.from(wishProducts));
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ProductIdRequest productIdRequest) {
        wishProductService.remove(userDetails.email(), productIdRequest.productId());
        return ResponseEntity.ok().build();
    }
}
