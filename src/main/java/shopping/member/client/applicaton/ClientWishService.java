package shopping.member.client.applicaton;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.member.client.applicaton.dto.WishProductResponse;
import shopping.member.client.domain.Client;
import shopping.product.application.event.UnWishEvent;
import shopping.product.application.event.WishEvent;

@Service
@RequiredArgsConstructor
public class ClientWishService {

    private final WishProductMapper wishProductMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void wish(Long productId, Client client) {
        wishProductMapper.validateProduct(productId);
        client.wish(productId);
        eventPublisher.publishEvent(new WishEvent(productId));
    }

    @Transactional
    public void unWish(Long productId, Client client) {
        client.unWish(productId);
        eventPublisher.publishEvent(new UnWishEvent(productId));
    }

    public List<WishProductResponse> findAll(Client client) {
        return wishProductMapper.createResponse(client.productIds());
    }
}
