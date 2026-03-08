package shopping.wish.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.exception.ApiException;
import shopping.common.exception.ErrorType;
import shopping.wish.domain.WishEntity;
import shopping.wish.domain.WishErrorMessage;
import shopping.wish.domain.WishRepository;

@Service
@RequiredArgsConstructor
public class WishDeleteUseCase {

    private final WishRepository wishRepository;

    @Transactional
    public Long execute(Long memberId, Long wishId) {
        WishEntity wish = wishRepository.findByIdAndMemberId(wishId, memberId)
                .orElseThrow(() -> new ApiException(WishErrorMessage.NOT_FOUND.getDescription(), ErrorType.NO_RESOURCE, HttpStatus.NOT_FOUND));
        wishRepository.delete(wish);
        return wish.getId();
    }
}
