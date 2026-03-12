package shopping.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.controller.dto.WishlistItemRequest;
import shopping.controller.dto.WishlistItemResponse;
import shopping.domain.WishlistItem;
import shopping.service.WishlistItemService;

import java.util.List;

@RestController
public class WishlistItemController {
    private final WishlistItemService service;

    public WishlistItemController(WishlistItemService service) {
        this.service = service;
    }

    @PostMapping("/wishlist")
    public ResponseEntity<Void> create(@RequestAttribute("memberId") Long memberId,
            @RequestBody WishlistItemRequest request) {
        service.addWishlistItem(new WishlistItem(memberId, request.getProductId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/wishlist")
    public ResponseEntity<List<WishlistItemResponse>> getWish(@RequestAttribute("memberId") Long memberId) {
        List<WishlistItem> items = service.findWishlistItems(memberId);
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
