package shopping.product.domain;

import java.util.UUID;

public record ProductDeletedEvent(UUID productId) {
}
