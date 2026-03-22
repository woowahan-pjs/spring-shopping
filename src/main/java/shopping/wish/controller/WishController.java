package shopping.wish.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shopping.auth.CustomUserPrincipal;
import shopping.wish.dto.WishProductResponse;
import shopping.wish.service.WishService;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishservice) {
        this.wishService = wishservice;
    }

    @PostMapping
    public void addWish(@AuthenticationPrincipal CustomUserPrincipal user, @RequestParam Long productId) {

        wishService.addWish(user.getMemberId(), productId);
    }

    @DeleteMapping("/{wishId}")
    public void deleteWish(@AuthenticationPrincipal CustomUserPrincipal user, @PathVariable Long wishId) {
        wishService.deleteWish(user.getMemberId(), wishId);
    }

    @GetMapping
    public List<WishProductResponse> getWishList (@AuthenticationPrincipal CustomUserPrincipal user) {
        return wishService.getWishList(user.getMemberId());
    }
}
