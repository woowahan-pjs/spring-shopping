package shopping.product.repository;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import shopping.product.domain.ProductName;

@Converter(autoApply = true)
public class ProductNameConverter implements AttributeConverter<ProductName, String> {
    @Override
    public String convertToDatabaseColumn(final ProductName name) {
        return name.getName();
    }

    @Override
    public ProductName convertToEntityAttribute(final String name) {
        return new ProductName(name);
    }
}
