package shopping.product.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.exception.ApiException;
import shopping.common.exception.ErrorType;
import shopping.product.application.dto.ProductCreateRequest;
import shopping.product.application.dto.ProductCreateResponse;
import shopping.product.domain.ProductEntity;
import shopping.product.domain.ProductErrorMessage;
import shopping.product.domain.ProductRepository;
import shopping.product.domain.ProfanityChecker;

@Service
@RequiredArgsConstructor
public class ProductCreateUseCase {

    private final ProductRepository productRepository;
    private final ProfanityChecker profanityChecker;

    @Transactional
    public ProductCreateResponse execute(ProductCreateRequest request) {
        if (profanityChecker.containsProfanity(request.name())) {
            throw new ApiException(ProductErrorMessage.PROFANITY_DETECTED.getDescription(),
                ErrorType.INVALID_PARAMETER, HttpStatus.BAD_REQUEST);
        }
        ProductEntity product = new ProductEntity(request.name(), request.price(),
            request.imageUrl());
        return ProductCreateResponse.from(productRepository.save(product));
    }
}
