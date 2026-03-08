package shopping.wish.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.exception.ApiException;
import shopping.common.exception.ErrorType;
import shopping.product.domain.ProductErrorMessage;
import shopping.product.domain.ProductRepository;
import shopping.wish.application.dto.WishAddRequest;
import shopping.wish.application.dto.WishAddResponse;
import shopping.wish.domain.WishEntity;
import shopping.wish.domain.WishErrorMessage;
import shopping.wish.domain.WishRepository;

@Service
@RequiredArgsConstructor
public class WishAddUseCase {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    @Transactional
    public WishAddResponse execute(Long memberId, WishAddRequest request) {
        productRepository.findByIdNotDeleted(request.productId())
                .orElseThrow(() -> new ApiException(ProductErrorMessage.NOT_FOUND.getDescription(), ErrorType.NO_RESOURCE, HttpStatus.NOT_FOUND));

        if (wishRepository.existsByMemberIdAndProductIdWithLock(memberId, request.productId())) {
            throw new ApiException(WishErrorMessage.ALREADY_EXISTS.getDescription(), ErrorType.INVALID_PARAMETER, HttpStatus.CONFLICT);
        }

        WishEntity wish = wishRepository.save(new WishEntity(memberId, request.productId()));
        return WishAddResponse.from(wish);
    }
}
