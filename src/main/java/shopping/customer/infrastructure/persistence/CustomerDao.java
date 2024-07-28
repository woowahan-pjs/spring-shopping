package shopping.customer.infrastructure.persistence;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import shopping.customer.infrastructure.api.dto.CustomerInfo;

import javax.sql.DataSource;

@Component
public class CustomerDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CustomerDao(final DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public CustomerInfo findById(final long customerId) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", customerId);

        final String sql = """
                SELECT c.name
                FROM customers c
                WHERE c.id = :id
                """;
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, new CustomerInfoMapper());
    }
}
