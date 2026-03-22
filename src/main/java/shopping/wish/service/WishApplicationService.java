package shopping.wish.service;

import shopping.wish.domain.*;
import shopping.wish.dto.WishResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import shopping.auth.AuthenticationService;
import shopping.product.domain.FindProduct;
import shopping.product.dto.ProductResponse;

@Service
public class WishApplicationService {

    private static final Logger log = LoggerFactory.getLogger(WishApplicationService.class);

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
        List<Wish> wishes = findWish.execute(memberId);
        List<UUID> productIds = wishes.stream().map(Wish::getProductId).toList();
        Map<UUID, ProductResponse> productMap = findProduct.execute(productIds).stream()
                .collect(Collectors.toMap(ProductResponse::id, Function.identity()));
        return wishes.stream().filter(wish -> {
            if (!productMap.containsKey(wish.getProductId())) {
                log.warn("Wish {} references non-existent product {}", wish.getId(),
                        wish.getProductId());
                return false;
            }
            return true;
        }).map(wish -> WishResponse.of(wish, productMap.get(wish.getProductId()))).toList();
    }
}
