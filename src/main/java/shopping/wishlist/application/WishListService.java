package shopping.wishlist.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.constant.enums.YesNo;
import shopping.exception.NotFoundException;
import shopping.product.application.ProductService;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;
import shopping.product.dto.ProductRequest;
import shopping.product.dto.ProductResponse;
import shopping.wishlist.domain.WishList;
import shopping.wishlist.domain.WishListRepository;
import shopping.wishlist.dto.WishListRequest;
import shopping.wishlist.dto.WishListResponse;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class WishListService {
    private WishListRepository wishListRepository;
    private ProductService productService;

    public WishListService(ProductService productService, WishListRepository wishListRepository) {
        this.productService = productService;
        this.wishListRepository = wishListRepository;
    }



    @Transactional
    public WishListResponse.WishDetail addWishList(WishListRequest.RegWishList request) {
        Product product = productService.findProductByPrdctSn(request.getPrdctSn());
        WishList persistWishList = wishListRepository.save(request.toWishList(product));

        return WishListResponse.WishDetail.from(persistWishList);
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



}
