package shopping.wishlist.infrastructure.persistence;

import org.springframework.jdbc.core.RowMapper;
import shopping.wishlist.infrastructure.api.dto.WishListInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WishListMapper implements RowMapper<WishListInfo> {

    @Override
    public WishListInfo mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new WishListInfo(
                rs.getString("product_name"),
                rs.getLong("amount"),
                rs.getString("thumbnail_image_url")
        );
    }
}
