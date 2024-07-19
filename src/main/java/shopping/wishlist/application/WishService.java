package shopping.wishlist.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.exception.NotFoundException;
import shopping.product.application.ProductService;
import shopping.product.domain.Product;
import shopping.wishlist.domain.Wish;
import shopping.wishlist.domain.WishList;
import shopping.wishlist.domain.WishRepository;
import shopping.wishlist.dto.WishRequest;
import shopping.wishlist.dto.WishResponse;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class WishService {
    private WishRepository wishRepository;
    private ProductService productService;

    public WishService(ProductService productService, WishRepository wishRepository) {
        this.productService = productService;
        this.wishRepository = wishRepository;
    }


    @Transactional
    public WishResponse.WishDetail addWishList(WishRequest.RegWishList request) {
        Product product = productService.findProductByPrdctSn(request.getPrdctSn());
        Wish persistWishList = wishRepository.save(request.toWishList(product));

        return WishResponse.WishDetail.from(persistWishList);
    }

    public WishResponse.WishListRes findAllWishList(Long mbrSn) {
        WishList wishList = findWishListByMbrSn(mbrSn);
        return WishResponse.WishListRes.from(wishList);
    }

    @Transactional
    public WishResponse.WishDetail updateWishProductCntById(Long wishSn, WishRequest.ModWishProductCnt request) {
        Wish wish = findWishById(wishSn);
        wish.updateCnt(request.isAdd(), request.getCnt());
        return WishResponse.WishDetail.from(wish);
    }




//
//    public ProductResponse.Products findAllProducts() {
//        List<Product> products = productRepository.findAll();
//
//        return ProductResponse.Products.from(products);
//    }
//
//
//    public ProductResponse.ProductDetail findProductDetailResponseBySn(Long id) {
//        Product persistProduct = findProductByPrdctSn(id);
//        return ProductResponse.ProductDetail.from(persistProduct);
//    }
//
//    @Transactional
//    public ProductResponse.ProductDetail updateProductById(Long id, ProductRequest.ModProduct request) {
//        Product persistProduct = findProductByPrdctSn(id);
//        persistProduct.updateNameOrImage(request.getPrdctNm(), request.getImage());
//        return ProductResponse.ProductDetail.from(persistProduct);
//    }
//
//    @Transactional
//    public void deleteProductById(Long id) {
//        Product persistProduct = findProductByPrdctSn(id);
//        persistProduct.updateDelYn(YesNo.Y);
//    }
//
//
//    private Product findProductByPrdctSn(Long sn) {
//        return productRepository.findById(sn)
//                .orElseThrow(() -> new NotFoundException("해당 상품이 존재하지 않습니다."));
//    }


    private WishList findWishListByMbrSn(Long mbrSn) {
        List<Wish> wishList = wishRepository.findAllByMbrSn(mbrSn);
        return new WishList(wishList);
    }

    private Wish findWishById(Long wishSn) {
        return wishRepository.findById(wishSn)
                .orElseThrow(() -> new NotFoundException("해당 위시 리스트가 존재하지 않습니다."));
    }


}
