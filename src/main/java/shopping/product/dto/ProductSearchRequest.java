package shopping.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;
import shopping.product.domain.Price;

@ParameterObject
@Schema(name = "[상품] 상품 검색 요청 파라미터 DTO", description = "등록된 상품 정보를 검색하기 위한 DTO 입니다.")
public record ProductSearchRequest(

    @Schema(description = "상품명 검색어")
    String name,

    @Schema(description = "최소 금액")
    Price fromPrice,

    @Schema(description = "최대 금액")
    Price toPrice
) {

}
