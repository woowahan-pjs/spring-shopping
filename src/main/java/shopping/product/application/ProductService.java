package shopping.product.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.application.dto.ProductCreateRequest;
import shopping.product.application.dto.ProductModifyRequest;
import shopping.product.application.dto.ProductResponse;
import shopping.product.domain.Product;
import shopping.product.exception.ProductNotExistException;
import shopping.product.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse save(final ProductCreateRequest createRequest) {
        final Product product = productRepository.save(ProductCreateRequest.toEntity(createRequest));

        return ProductResponse.from(product);
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    public ProductResponse findById(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotExistException("상품이 존재하지 않습니다."));
        return ProductResponse.from(product);
    }

    @Transactional
    public void update(final Long id, final ProductModifyRequest request) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotExistException("상품이 존재하지 않습니다."));

        product.modifyName(request.getName());
        product.modifyImagePath(request.getImagePath());
        product.modifyAmount(request.getAmount());
        product.modifyPrice(request.getPrice());
    }

    public void delete(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotExistException("상품이 존재하지 않습니다."));

        productRepository.delete(product);
    }
}
