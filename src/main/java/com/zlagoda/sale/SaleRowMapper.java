package com.zlagoda.sale;

import com.zlagoda.check.Check;
import com.zlagoda.store.product.StoreProduct;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class SaleRowMapper implements RowMapper<Sale>  {
    @Override
    public Sale mapRow(ResultSet rs, int rowNum) throws SQLException {
       return new Sale(
               StoreProduct.builder().upc(rs.getString("UPC")).build(),
               Check.builder().checkNumber(rs.getString("check_number")).build(),
               rs.getInt("product_number"),
               rs.getBigDecimal("selling_price")
       );
    }
}
