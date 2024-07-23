package shopping.product.infrastructure.api.dto;

public record ProductDetailInfo(String productName, long amount, String imageUrl, String categoryName, String shopName,
                                String sellerName) {
}

