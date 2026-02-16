package shopping.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import shopping.category.Category;

public record ProductRequest(
    @NotBlank String name,
    @Positive int price,
    String imageUrl,
    @NotNull Long categoryId
) {
    public Product toEntity(Category category) {
        return new Product(name, price, imageUrl, category);
    }
}
