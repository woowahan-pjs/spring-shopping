package shopping.product.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductErrorMessage {
    NOT_FOUND("존재하지 않는 상품입니다"),
    PROFANITY_DETECTED("상품 이름에 비속어를 사용할 수 없습니다");

    private final String description;
}
