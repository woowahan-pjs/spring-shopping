package shopping.wishlist.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.common.PageResponse;
import shopping.product.controller.dto.ProductResponse;
import shopping.wishlist.controller.dto.WishlistRequest;
import shopping.wishlist.controller.dto.WishlistResponse;
import shopping.wishlist.domain.Wishlist;
import shopping.wishlist.service.WishlistService;

import java.util.List;

@RestController
public class WishlistController {
    private final WishlistService service;

    public WishlistController(WishlistService service) {
        this.service = service;
    }

    @PostMapping("/wishlist")
    public ResponseEntity<Void> create(@RequestAttribute("memberId") Long memberId,
            @RequestBody WishlistRequest request) {
        service.addWishlist(new Wishlist(memberId, request.productId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/wishlist")
    public ResponseEntity<PageResponse<WishlistResponse>> getWish(
            @RequestAttribute("memberId") Long memberId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<WishlistResponse> page = service.findWishlistItems(memberId, pageable)
                .map(WishlistResponse::from);
        return ResponseEntity.ok(PageResponse.of(page));
    }

    @DeleteMapping("/wishlist/{id}")
    public ResponseEntity<Void> deleteWish(@RequestAttribute("memberId") Long memberId,
                                           @PathVariable("id") Long id) {
        service.deleteWishlistItem(memberId, id);
        return ResponseEntity.noContent().build();
    }
}
