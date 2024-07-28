package shopping.product.infrastructure.persistence;

import org.springframework.jdbc.core.RowMapper;
import shopping.product.infrastructure.api.dto.ProductInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductListMapper implements RowMapper<ProductInfo> {

    @Override
    public ProductInfo mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new ProductInfo(
                rs.getString("product_name"),
                rs.getLong("amount"),
                rs.getString("image_url")
        );
    }
}