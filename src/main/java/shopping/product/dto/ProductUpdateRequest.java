package shopping.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import shopping.product.domain.Price;

@Schema(name = "[상품] 상품 수정 요청 DTO", description = "상품 정보를 수정하기 위한 DTO 입니다.")
public record ProductUpdateRequest(
    @Pattern(
        regexp = "^(?=.*\\S)[\\p{L}\\p{M}\\p{N} ()\\[\\]&\\-+/_,]+$",
        message = "상품명은 문자/숫자/공백과 특수문자 (, ), [, ], &, -, +, /, _, , 만 입력 가능하며 공백만은 불가합니다."
    )
    @Size(min = 5, max = 15, message = "상품명은 최소 {min}자에서 최대 {max}자 까지 입력 가능합니다.")
    @Schema(description = "상품명", example = "ふろっこど～る なつめ") String name,

    @Valid @Schema(description = "가격", example = "1650") Price price,

    @URL(message = "http:// 또는 https:// 형식만 입력 가능합니다.")
    @Size(min = 9, max = 255, message = "이미지 URL은 최소 {min}자에서 최대 {max}자 까지 입력 가능합니다.")
    @Schema(
        description = "이미지 URL",
        example =
            "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg")
    String imageUrl
) {

}
