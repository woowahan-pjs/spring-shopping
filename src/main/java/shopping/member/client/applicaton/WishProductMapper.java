package shopping.member.client.applicaton;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping.member.client.applicaton.dto.WishProductResponse;
import shopping.product.application.ProductService;

@Service
@RequiredArgsConstructor
public class WishProductMapper {

    private final ProductService productService;

    public void validateProduct(Long productId) {
        productService.findProduct(productId);
    }

    public List<WishProductResponse> createResponse(List<Long> productIds) {
        return productService.findProducts(productIds)
                .stream()
                .map(product -> new WishProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImage())
                )
                .toList();
    }
}
