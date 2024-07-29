package shopping.customer.infrastructure.persistence;

import org.springframework.jdbc.core.RowMapper;
import shopping.customer.infrastructure.api.dto.CustomerInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerInfoMapper implements RowMapper<CustomerInfo> {
    @Override
    public CustomerInfo mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new CustomerInfo(rs.getString("name"));
    }
}
