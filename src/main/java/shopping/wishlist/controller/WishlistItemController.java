package shopping.wishlist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.wishlist.controller.dto.WishlistItemRequest;
import shopping.wishlist.controller.dto.WishlistItemResponse;
import shopping.wishlist.domain.Wishlist;
import shopping.wishlist.service.WishlistService;

import java.util.List;

@RestController
public class WishlistItemController {
    private final WishlistService service;

    public WishlistItemController(WishlistService service) {
        this.service = service;
    }

    @PostMapping("/wishlist")
    public ResponseEntity<Void> create(@RequestAttribute("memberId") Long memberId,
            @RequestBody WishlistItemRequest request) {
        service.addWishlist(new Wishlist(memberId, request.getProductId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/wishlist")
    public ResponseEntity<List<WishlistItemResponse>> getWish(@RequestAttribute("memberId") Long memberId) {
        List<Wishlist> items = service.findWishlistItems(memberId);
        return ResponseEntity.ok(items.stream()
                .map(WishlistItemResponse::from)
                .toList());
    }

    @DeleteMapping("/wishlist/{id}")
    public ResponseEntity<Void> deleteWish(@RequestAttribute("memberId") Long memberId,
                                           @PathVariable("id") Long id) {
        service.deleteWishlistItem(memberId, id);
        return ResponseEntity.noContent().build();
    }
}
