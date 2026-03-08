package shopping.product.dto;

import java.util.List;

import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.media.Schema;
import shopping.product.domain.Product;

@Schema(name = "[상품] 상품 리스트 검색 응답 DTO", description = "검색된 상품 리스트의 DTO 입니다.")
public record ProductsSearchResponse(List<ProductResponse> products, Pageable pageable) {

    public static ProductsSearchResponse from(
            final List<Product> products, final Pageable pageable) {
        return new ProductsSearchResponse(
                products.stream()
                        .map(
                                it ->
                                        ProductResponse.from(
                                                it.getId(),
                                                it.getName(),
                                                it.getPrice(),
                                                it.getImageUrl()))
                        .toList(),
                pageable);
    }
}
