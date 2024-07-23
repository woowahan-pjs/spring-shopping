package shopping.product.infrastructure.persistence;

import org.springframework.jdbc.core.RowMapper;
import shopping.product.infrastructure.api.dto.ProductDetailInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<ProductDetailInfo> {

    @Override
    public ProductDetailInfo mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new ProductDetailInfo(
                rs.getString("product_name"),
                rs.getLong("amount"),
                rs.getString("image_url"),
                rs.getString("category_name"),
                rs.getString("shop_name"),
                rs.getString("seller_name")
        );
    }
}