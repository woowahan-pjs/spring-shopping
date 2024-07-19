package shopping.wishlist.ui;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        WishResponse.WishDetail wishList = wishService.addWishList(request);
        return ResponseEntity.created(URI.create("/wishList/" + wishList.getWishSn())).body(wishList);
    }

    @GetMapping("member/{id}")
    public ResponseEntity<WishResponse.WishListRes> findAllWishList(@PathVariable Long id) {
        WishResponse.WishListRes wishList = wishService.findAllWishList(id);
        return ResponseEntity.ok().body(wishList);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ProductResponse.ProductDetail> findProductBySn(@PathVariable Long id) {
//        return ResponseEntity.ok().body(productService.findProductDetailResponseBySn(id));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ProductResponse.ProductDetail> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest.ModProduct request) {
//        ProductResponse.ProductDetail product = productService.updateProductById(id, request);
//        return ResponseEntity.ok().body(product);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity deleteProduct(@PathVariable Long id) {
//        productService.deleteProductById(id);
//        return ResponseEntity.noContent().build();
//    }

}
