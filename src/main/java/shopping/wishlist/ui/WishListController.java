package shopping.wishlist.ui;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.product.application.ProductService;
import shopping.product.dto.ProductRequest;
import shopping.product.dto.ProductResponse;
import shopping.wishlist.application.WishListService;
import shopping.wishlist.dto.WishListRequest;
import shopping.wishlist.dto.WishListResponse;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/wishLists")
public class WishListController {

    private WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @PostMapping()
    public ResponseEntity<WishListResponse.WishDetail> addWishList(@RequestBody @Valid WishListRequest.RegWishList request) {
        WishListResponse.WishDetail wishList = wishListService.addWishList(request);
        return ResponseEntity.created(URI.create("/wishLists/" + wishList.getWishSn())).body(wishList);
    }

//    @GetMapping()
//    public ResponseEntity<ProductResponse.Products> findAllProducts() {
//        ProductResponse.Products products = productService.findAllProducts();
//        return ResponseEntity.ok().body(products);
//    }
//
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
