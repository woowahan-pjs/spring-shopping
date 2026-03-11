package shopping.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shopping.controller.dto.WishlistItemRequest;
import shopping.domain.WishlistItem;
import shopping.service.WishlistItemService;

@RestController
public class WishlistItemController {

    private WishlistItemService service;

    public WishlistItemController(WishlistItemService service) {
        this.service = service;
    }

    @PostMapping("/wishlist")
    public ResponseEntity<Void> create(@RequestAttribute("memberId") Long memberId,
            @RequestBody WishlistItemRequest request) {
        service.addWishlistItem(new WishlistItem(memberId, request.getProductId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
