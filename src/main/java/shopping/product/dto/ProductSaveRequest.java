package shopping.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import shopping.product.domain.Price;
import shopping.product.domain.ValidProductImageUrl;
import shopping.product.domain.ValidProductName;

@Schema(name = "[상품] 상품 저장 요청 DTO", description = "상품 저장을 위한 DTO 입니다.")
public record ProductSaveRequest(

    @ValidProductName
    @Schema(description = "상품명", example = "ふろっこど-る なつめ")
    String name,

    @Valid
    @Schema(description = "가격", example = "1650") Price price,
    @ValidProductImageUrl
    @Schema(description = "이미지 URL", example = "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg")
    String imageUrl
) {

}
