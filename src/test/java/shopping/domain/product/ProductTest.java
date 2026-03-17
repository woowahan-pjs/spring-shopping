package shopping.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.domain.product.exception.InvalidPriceException;
import shopping.domain.product.exception.ProductNameBlankException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("상품 생성")
    void shouldCreateProduct() {
        String name = "T-Shirt";
        BigDecimal price = new BigDecimal("19.99");
        String imageUrl = "t-shirt.jpg";

        Product product = new Product(name, price, imageUrl);

        assertEquals(name, product.getName());
        assertEquals(price, product.getPrice());
        assertEquals(imageUrl, product.getImageUrl());
        assertNull(product.getId(), "ID should be null for new non-persisted entities");
    }

    @Test
    @DisplayName("상품에 유효하지 않은 값을 넣을 경우 exception")
    void shouldThrowExceptionForInvalidData() {
        // Name validation
        assertThrows(ProductNameBlankException.class, () -> new Product(null, BigDecimal.TEN, "img.jpg"));
        assertThrows(ProductNameBlankException.class, () -> new Product("", BigDecimal.TEN, "img.jpg"));
        assertThrows(ProductNameBlankException.class, () -> new Product("   ", BigDecimal.TEN, "img.jpg"));

        // Price validation
        assertThrows(InvalidPriceException.class, () -> new Product("Name", null, "img.jpg"));
        assertThrows(InvalidPriceException.class, () -> new Product("Name", new BigDecimal("-1"), "img.jpg"));
    }
}