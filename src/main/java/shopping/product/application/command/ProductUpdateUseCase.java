package shopping.product.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.exception.ApiException;
import shopping.common.exception.ErrorType;
import shopping.product.application.dto.ProductUpdateRequest;
import shopping.product.application.dto.ProductUpdateResponse;
import shopping.product.domain.ProductEntity;
import shopping.product.domain.ProductErrorMessage;
import shopping.product.domain.ProductRepository;
import shopping.product.domain.ProfanityChecker;

@Service
@RequiredArgsConstructor
public class ProductUpdateUseCase {

    private final ProductRepository productRepository;
    private final ProfanityChecker profanityChecker;

    @Transactional
    public ProductUpdateResponse execute(Long productId, ProductUpdateRequest request) {
        if (request.name() != null && profanityChecker.containsProfanity(request.name())) {
            throw new ApiException(ProductErrorMessage.PROFANITY_DETECTED.getDescription(), ErrorType.INVALID_PARAMETER, HttpStatus.BAD_REQUEST);
        }
        ProductEntity product = productRepository.findByIdNotDeleted(productId)
                .orElseThrow(() -> new ApiException(ProductErrorMessage.NOT_FOUND.getDescription(), ErrorType.NO_RESOURCE, HttpStatus.NOT_FOUND));
        product.update(request.name(), request.price(), request.imageUrl());
        return ProductUpdateResponse.from(product);
    }
}
