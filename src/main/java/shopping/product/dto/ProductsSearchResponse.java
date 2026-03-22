package shopping.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Pageable;
import shopping.product.domain.PageInfo;
import shopping.product.domain.Product;

import java.util.List;

@Schema(name = "[상품] 상품 리스트 검색 응답 DTO", description = "검색된 상품 리스트의 DTO 입니다.")
public record ProductsSearchResponse(
    List<ProductResponse> products,
    PageInfo pageInfo
) {

    public static ProductsSearchResponse from(final List<Product> products, final Pageable pageable) {
        return new ProductsSearchResponse(
                products.stream().map(it -> ProductResponse.from(
                        it.getId(),
                        it.getName(),
                        it.getPrice(),
                        it.getImageUrl()))
                    .toList(),
                PageInfo.from(pageable));
    }
}
