package shopping.wish.service;

import shopping.wish.domain.*;
import shopping.wish.dto.WishResponse;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import shopping.auth.AuthenticationService;
import shopping.product.domain.FindProduct;
import shopping.product.dto.ProductResponse;

@Service
public class WishApplicationService {

    private final AddWish addWish;
    private final RemoveWish removeWish;
    private final FindWishWithProductService findWishWithProductService;
    private final FindProduct findProduct;
    private final AuthenticationService authenticationService;

    public WishApplicationService(AddWish addWish, RemoveWish removeWish,
            FindWishWithProductService findWishWithProductService, FindProduct findProduct,
            AuthenticationService authenticationService) {
        this.addWish = addWish;
        this.removeWish = removeWish;
        this.findWishWithProductService = findWishWithProductService;
        this.findProduct = findProduct;
        this.authenticationService = authenticationService;
    }

    public WishResponse add(String authorization, UUID productId) {
        UUID memberId = authenticationService.extractMemberId(authorization);
        ProductResponse product = findProduct.execute(productId);
        Wish wish = addWish.execute(memberId, productId, product.price());
        return WishResponse.of(wish, product);
    }

    public void remove(String authorization, UUID productId) {
        UUID memberId = authenticationService.extractMemberId(authorization);
        removeWish.execute(memberId, productId);
    }

    public List<WishResponse> findAll(String authorization) {
        UUID memberId = authenticationService.extractMemberId(authorization);
        return findWishWithProductService.execute(memberId);
    }
}
