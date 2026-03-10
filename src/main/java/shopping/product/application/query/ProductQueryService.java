package shopping.product.application.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.exception.ApiException;
import shopping.common.exception.ErrorType;
import shopping.product.application.dto.ProductGetResponse;
import shopping.product.domain.ProductErrorMessage;
import shopping.product.domain.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductRepository productRepository;

    public ProductGetResponse getById(Long productId) {
        return productRepository.findByIdNotDeleted(productId)
            .map(ProductGetResponse::from)
            .orElseThrow(() -> new ApiException(ProductErrorMessage.NOT_FOUND.getDescription(),
                ErrorType.NO_RESOURCE, HttpStatus.NOT_FOUND));
    }

    public List<ProductGetResponse> getAll() {
        return productRepository.findAllNotDeleted().stream()
            .map(ProductGetResponse::from)
            .toList();
    }

}
