package shopping.product.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProductException("해당 상품을 찾을 수 없습니다. " + id));
    }

    public List<Product> findProducts(List<Long> ids){
        return productRepository.findByIdIn(ids);
    }
}
