package shopping.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.application.event.UnWishEvent;
import shopping.product.application.event.WishEvent;
import shopping.product.domain.Product;

@Component
@RequiredArgsConstructor
public class WishEventHandler {

    private final ProductService productService;

    @Async
    @EventListener
    @Transactional
    public void handle(WishEvent wishEvent) {
        final Product product = productService.findProduct(wishEvent.productId());
        product.wish();
    }

    @Async
    @EventListener
    @Transactional
    public void handle(UnWishEvent wishEvent) {
        final Product product = productService.findProduct(wishEvent.productId());
        product.unWish();
    }
}
