package shopping.wishilist.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shopping.auth.ui.AuthenticationPrincipal;
import shopping.auth.ui.UserPrincipal;
import shopping.wishilist.application.WishlistService;
import shopping.wishilist.application.dto.WishlistRequest;
import shopping.wishilist.application.dto.WishlistResponse;

import java.net.URI;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(final WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<WishlistResponse> addProduct(@AuthenticationPrincipal final UserPrincipal userPrincipal,
                                                       @RequestBody final WishlistRequest request) {
        final WishlistResponse wishlistResponse = wishlistService.addProduct(userPrincipal.getId(), request);
        return ResponseEntity.created(URI.create("/wishlist/" + wishlistResponse.getId())).body(wishlistResponse);
    }

    @GetMapping
    public ResponseEntity<WishlistResponse> findWishlist(@AuthenticationPrincipal final UserPrincipal userPrincipal) {
        return ResponseEntity.ok().body(wishlistService.findOrCreateByMember(userPrincipal.getId()));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeProduct(@AuthenticationPrincipal final UserPrincipal userPrincipal,
                                              @RequestParam("productId") final Long productId) {
        wishlistService.deleteProduct(userPrincipal.getId(), productId);
        return ResponseEntity.noContent().build();
    }
}
