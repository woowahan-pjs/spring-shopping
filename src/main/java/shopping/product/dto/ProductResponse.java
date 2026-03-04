package shopping.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import shopping.product.domain.Price;

@Schema(name = "[상품] 상품 조회 응답 DTO", description = "상품 조회시, 상품 정보를 담은 DTO 입니다.")
public record ProductResponse(
        @Schema(description = "상품 고유 ID", example = "703") Long productId,
        @Schema(description = "상품명", example = "ふろっこど～る なつめ") String name,
        @Schema(description = "가격", example = "1650") Price price,
        @Schema(
                        description = "이미지 URL",
                        example =
                                "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg")
                String imageUrl) {

    public static ProductResponse from(
            final Long productId, final String name, final Price price, final String imageUrl) {
        return new ProductResponse(productId, name, price, imageUrl);
    }
}
