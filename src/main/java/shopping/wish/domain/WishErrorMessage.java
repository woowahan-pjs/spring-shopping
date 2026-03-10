package shopping.wish.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WishErrorMessage {
    NOT_FOUND("위시리스트 항목을 찾을 수 없습니다"),
    ALREADY_EXISTS("이미 위시리스트에 추가된 상품입니다");

    private final String description;
}
