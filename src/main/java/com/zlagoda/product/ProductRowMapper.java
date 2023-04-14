package com.zlagoda.product;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductRowMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Product(
                rs.getLong("id_product"),
                rs.getString("product_name"),
                rs.getString("characteristics"),
                rs.getDate("expiration_date"),
                rs.getLong("category_number")
        );
    }
}