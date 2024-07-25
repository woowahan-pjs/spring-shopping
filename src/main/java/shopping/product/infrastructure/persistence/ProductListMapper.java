package shopping.product.infrastructure.persistence;

import org.springframework.jdbc.core.RowMapper;
import shopping.product.infrastructure.api.dto.ProductList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductListMapper implements RowMapper<ProductList> {

    @Override
    public ProductList mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new ProductList(
                rs.getString("product_name"),
                rs.getLong("amount"),
                rs.getString("image_url")
        );
    }
}