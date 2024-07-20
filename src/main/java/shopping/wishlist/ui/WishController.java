package shopping.wishlist.ui;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.auth.domain.AuthenticationPrincipal;
import shopping.auth.domain.LoginMember;
import shopping.product.dto.ProductRequest;
import shopping.product.dto.ProductResponse;
import shopping.wishlist.application.WishService;
import shopping.wishlist.dto.WishRequest;
import shopping.wishlist.dto.WishResponse;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/wishList")
public class WishController {

    private WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping()
    public ResponseEntity<WishResponse.WishDetail> addWishList(@RequestBody @Valid WishRequest.RegWishList request) {
        WishResponse.WishDetail wish = wishService.addWishList(request);
        return ResponseEntity.created(URI.create("/wishList/" + wish.getWishSn())).body(wish);
    }

    @GetMapping("member")
    public ResponseEntity<WishResponse.WishListRes> findAllWishList(@AuthenticationPrincipal LoginMember loginMember) {
        WishResponse.WishListRes wishList = wishService.findAllWishList(loginMember.getId());
        return ResponseEntity.ok().body(wishList);
    }

    @PutMapping("/cnt/{id}")
    public ResponseEntity<WishResponse.WishDetail> updateWishProductCnt(@PathVariable Long id, @RequestBody @Valid WishRequest.ModWishProductCnt request) {
        WishResponse.WishDetail wish = wishService.updateWishProductCntById(id, request);
        return ResponseEntity.ok().body(wish);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteWish(@PathVariable Long id) {
        wishService.deleteWishById(id);
        return ResponseEntity.noContent().build();
    }

}
