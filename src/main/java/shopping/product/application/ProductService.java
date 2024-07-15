package shopping.product.application;

import org.springframework.stereotype.Service;
import shopping.product.application.dto.ProductRequest;
import shopping.product.application.dto.ProductResponse;

import java.util.List;

@Service
public class ProductService {
    public ProductResponse save(final ProductRequest createRequest) {
        return null;
    }

    public List<ProductResponse> findAll() {
        return null;
    }

    public ProductResponse findById(final Long id) {
        return null;
    }

    public void update(final Long id, final ProductRequest request) {

    }

    public void delete(final Long id) {

    }
}
