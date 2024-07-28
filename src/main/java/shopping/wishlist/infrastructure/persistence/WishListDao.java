package shopping.wishlist.infrastructure.persistence;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import shopping.product.infrastructure.persistence.ProductListMapper;
import shopping.wishlist.infrastructure.api.dto.WishListInfo;

import javax.sql.DataSource;
import java.util.List;

@Component
public class WishListDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public WishListDao(final DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<WishListInfo> findByCustomerId(final long customerId, final long startId, final long limit) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("customerId", customerId)
                .addValue("startId", startId)
                .addValue("limit", limit);

        return namedParameterJdbcTemplate.query(
                """
                        SELECT 
                            p.name as product_name, p.amount, p.image_url, sc.name as category_name, s.name as shop_name, se.name as seller_name   
                        FROM wish_lists w
                        LEFT JOIN products p ON p.id = w.product_id
                        LEFT JOIN shops s ON p.shop_id = s.id 
                        LEFT JOIN sub_categories sc ON p.sub_category_id = sc.id 
                        LEFT JOIN sellers se ON s.seller_id = se.id
                        WHERE p.id >= :startId 
                        AND w.customer_id = :customerId 
                        LIMIT :limit
                        """,
                namedParameters, new WishListMapper()
        );
    }
}
