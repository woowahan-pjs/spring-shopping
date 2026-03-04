package shopping.infra.client.purgomalum;

/**
 * ContainsProfanityRequest 클래스는 특정 텍스트가 부적절한 언어(profanity)를 포함하는지 확인하기 위한 요청 데이터를 나타냅니다.
 * 주로 PurgoMalum 서비스와의 통신에 사용됩니다.
 */
public record ContainsProfanityRequest(
    /* 검증하기 위한 텍스트, 필수 값 */
    String text
) {

    @Override
    public String toString() {
        return "?" +
            "text=" + text;
    }
}
