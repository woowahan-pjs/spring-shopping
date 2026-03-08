package shopping.product.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.exception.ApiException;
import shopping.common.exception.ErrorType;
import shopping.product.domain.ProductEntity;
import shopping.product.domain.ProductErrorMessage;
import shopping.product.domain.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductDeleteUseCase {

    private final ProductRepository productRepository;

    @Transactional
    public Long execute(Long productId) {
        ProductEntity product = productRepository.findByIdNotDeleted(productId)
            .orElseThrow(() -> new ApiException(ProductErrorMessage.NOT_FOUND.getDescription(),
                ErrorType.NO_RESOURCE, HttpStatus.NOT_FOUND));
        product.delete();
        return product.getId();
    }
}
