package com.zlagoda.card;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CustomerCardRowMapper implements RowMapper<CustomerCard> {

    @Override
    public CustomerCard mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CustomerCard(
                rs.getString("card_number"),
                rs.getString("cust_surname"),
                rs.getString("cust_name"),
                rs.getString("cust_patronymic"),
                rs.getString("phone_number"),
                rs.getString("city"),
                rs.getString("street"),
                rs.getString("zip_code"),
                rs.getInt("percent")
        );
    }
}