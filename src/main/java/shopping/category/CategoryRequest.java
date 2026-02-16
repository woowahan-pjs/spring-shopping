package shopping.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
    @NotBlank String name,
    String color,
    String imageUrl,
    String description
) {
    public Category toEntity() {
        return new Category(name, color, imageUrl, description);
    }
}
