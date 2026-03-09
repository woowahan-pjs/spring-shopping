package shopping.wish.controller;

import org.springframework.web.bind.annotation.*;
import shopping.wish.service.WishService;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishservice) {
        this.wishService = wishservice;
    }

    @PostMapping
    public void addWish(@RequestHeader Long memberId, @RequestParam Long productId) {

        wishService.addWish(memberId, productId);
    }

    @DeleteMapping("/{wishId}")
    public void deleteWish(@PathVariable Long wishId) {

        wishService.deleteWish(wishId);
    }
}
