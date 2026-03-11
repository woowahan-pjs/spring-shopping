package shopping.wish.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import shopping.product.domain.ProductDeletedEvent;
import shopping.wish.domain.RemoveWishByProduct;

@Component
public class WishEventListener {

    private final RemoveWishByProduct removeWishByProduct;

    public WishEventListener(RemoveWishByProduct removeWishByProduct) {
        this.removeWishByProduct = removeWishByProduct;
    }

    @EventListener
    public void handle(ProductDeletedEvent event) {
        removeWishByProduct.execute(event.productId());
    }
}
