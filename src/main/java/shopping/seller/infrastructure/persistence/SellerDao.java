package shopping.seller.infrastructure.persistence;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.seller.infrastructure.api.dto.SellerInfo;

@Component
public class SellerDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SellerDao(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional(readOnly = true)
    public SellerInfo findBySellerId(final long sellerId) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", sellerId);
        final String query = """
                SELECT s.name
                FROM sellers s
                WHERE s.id = :id
                """;
        return namedParameterJdbcTemplate.queryForObject(query, namedParameters, new SellerInfoRowMapper());
    }
}
