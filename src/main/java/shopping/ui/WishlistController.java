package shopping.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.application.wishlist.WishlistService;
import shopping.domain.member.Member;
import shopping.dto.WishlistRequest;
import shopping.dto.WishlistResponse;
import shopping.infrastructure.auth.LoginMember;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<Void> addWishlist(
            @LoginMember Member member,
            @RequestBody WishlistRequest request) {
        wishlistService.addWishlist(member.getId(), request.productId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getWishList(@LoginMember Member member) {
        return ResponseEntity.ok(wishlistService.getWishlist(member.getId()));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeWishlist(
            @LoginMember Member member,
            @PathVariable Long productId) {
        wishlistService.removeWishlist(member.getId(), productId);
        return ResponseEntity.noContent().build();
    }
}
