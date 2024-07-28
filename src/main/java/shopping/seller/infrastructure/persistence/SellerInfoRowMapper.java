package shopping.seller.infrastructure.persistence;

import org.springframework.jdbc.core.RowMapper;
import shopping.seller.infrastructure.api.dto.SellerInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SellerInfoRowMapper implements RowMapper<SellerInfo> {
    @Override
    public SellerInfo mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new SellerInfo(rs.getString("name"));
    }
}
