package shopping.wishlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import shopping.product.domain.Price;
import shopping.product.domain.Product;
import shopping.wishlist.domain.WishListItem;

@Schema(name = "[위시 리스트] 위시 리스트 상품 조회 응답 DTO", description = "위시 리스트의 상품 상세 정보를 담은 DTO 입니다.")
public record WishListItemResponse(
        @Schema(description = "위시 리스트 아이템 고유 ID", example = "79") Long wishId,
        @Schema(description = "상품 고유 ID", example = "703") Long productId,
        @Schema(description = "상품명", example = "ふろっこど-る なつめ") String name,
        @Schema(description = "가격", example = "1650") Price price,
        @Schema(
                        description = "이미지 URL",
                        example =
                                "https://tc-animate.techorus-cdn.com/resize_image/resize_image.php?image=4902273250051_1_1761649508.jpg")
                String imageUrl) {

    public static WishListItemResponse from(final WishListItem item) {
        final Product product = item.getProduct();

        return new WishListItemResponse(
                item.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl());
    }
}
