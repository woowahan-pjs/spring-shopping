package shopping.wishlist.presesntation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.auth.AuthenticationPrincipal;
import shopping.auth.UserDetails;
import shopping.wishlist.application.WishProductService;

@RequestMapping("/wish-products")
@RestController
public class WishProductController {
    private final WishProductService wishProductService;

    public WishProductController(WishProductService wishProductService) {
        this.wishProductService = wishProductService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@AuthenticationPrincipal UserDetails userDetails, @RequestBody WishProductRequest wishProductRequest) {
        wishProductService.add(userDetails.email(), wishProductRequest.productId());
        return ResponseEntity.ok().build();
    }
}
