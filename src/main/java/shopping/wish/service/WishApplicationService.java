package shopping.wish.service;

import shopping.wish.domain.*;
import shopping.wish.dto.WishResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import shopping.auth.AuthenticationService;
import shopping.product.domain.FindProduct;
import shopping.product.domain.Product;

@Service
public class WishApplicationService {

    private final AddWish addWish;
    private final RemoveWish removeWish;
    private final FindWish findWish;
    private final FindProduct findProduct;
    private final AuthenticationService authenticationService;

    public WishApplicationService(AddWish addWish, RemoveWish removeWish, FindWish findWish,
            FindProduct findProduct, AuthenticationService authenticationService) {
        this.addWish = addWish;
        this.removeWish = removeWish;
        this.findWish = findWish;
        this.findProduct = findProduct;
        this.authenticationService = authenticationService;
    }

    public WishResponse add(String authorization, UUID productId) {
        UUID memberId = authenticationService.extractMemberId(authorization);
        Product product = findProduct.execute(productId);
        Wish wish = addWish.execute(memberId, productId, product.getPrice());
        return WishResponse.of(wish, product);
    }

    public void remove(String authorization, UUID productId) {
        UUID memberId = authenticationService.extractMemberId(authorization);
        removeWish.execute(memberId, productId);
    }

    public List<WishResponse> findAll(String authorization) {
        UUID memberId = authenticationService.extractMemberId(authorization);
        return findWish.execute(memberId).stream().map(wish -> {
            try {
                return WishResponse.of(wish, findProduct.execute(wish.getProductId()));
            } catch (NoSuchElementException e) {
                return null;
            }
        }).filter(Objects::nonNull).toList();
    }
}
