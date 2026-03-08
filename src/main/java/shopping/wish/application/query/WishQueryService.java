package shopping.wish.application.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.exception.ApiException;
import shopping.common.exception.ErrorType;
import shopping.product.domain.ProductErrorMessage;
import shopping.product.domain.ProductEntity;
import shopping.product.domain.ProductRepository;
import shopping.wish.application.dto.WishGetResponse;
import shopping.wish.domain.WishRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishQueryService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public List<WishGetResponse> getAll(Long memberId) {
        return wishRepository.findAllByMemberId(memberId).stream()
                .map(wish -> {
                    ProductEntity product = productRepository.findByIdNotDeleted(wish.getProductId())
                            .orElseThrow(() -> new ApiException(ProductErrorMessage.NOT_FOUND.getDescription(), ErrorType.NO_RESOURCE, HttpStatus.NOT_FOUND));
                    return WishGetResponse.of(wish, product);
                })
                .toList();
    }
}
