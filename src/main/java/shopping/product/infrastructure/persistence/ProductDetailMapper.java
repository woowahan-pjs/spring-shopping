package shopping.product.infrastructure.persistence;

import org.springframework.jdbc.core.RowMapper;
import shopping.product.infrastructure.api.dto.ProductDetailResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDetailMapper implements RowMapper<ProductDetailResponse> {

    @Override
    public ProductDetailResponse mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new ProductDetailResponse(
                rs.getString("product_name"),
                rs.getLong("amount"),
                rs.getString("thumbnail_image_url"),
                rs.getString("category_name"),
                rs.getString("shop_name"),
                rs.getString("seller_name")
        );
    }
}