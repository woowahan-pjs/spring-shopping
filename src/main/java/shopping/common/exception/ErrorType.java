package shopping.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    NO_RESOURCE("존재하지 않는 데이터입니다"),
    INVALID_PARAMETER("잘못된 파라미터 요청입니다"),
    UNKNOWN_ERROR("알 수 없는 에러입니다"),
    EXTERNAL_API_ERROR("외부 API 호출 에러 입니다");
    private final String description;
}
