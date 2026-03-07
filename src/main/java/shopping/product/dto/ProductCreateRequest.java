package shopping.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductCreateRequest {

    @NotBlank
    @Size(max = 15)
    private String name;

    private int stockQuantity;

    @Min(0)
    private long price;

    private String imageUrl;
}
