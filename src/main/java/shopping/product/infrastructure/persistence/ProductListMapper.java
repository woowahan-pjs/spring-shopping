package shopping.product.infrastructure.persistence;

import org.springframework.jdbc.core.RowMapper;
import shopping.product.infrastructure.api.dto.ProductListResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductListMapper implements RowMapper<ProductListResponse> {

    @Override
    public ProductListResponse mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new ProductListResponse(
                rs.getString("product_name"),
                rs.getLong("amount"),
                rs.getString("image_url")
        );
    }
}