package com.zlagoda.check;

import com.zlagoda.card.CustomerCard;
import com.zlagoda.employee.Employee;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;

@Component
public class CheckRowMapper implements RowMapper<Check> {
    @Override
    public Check mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Check(
                rs.getString("check_number"),
                Employee.builder().id(rs.getString("id_employee")).build(),
                CustomerCard.builder().cardNumber(rs.getString("card_number")).build(),
                new LinkedHashSet<>(),
                rs.getDate("print_date"),
                rs.getBigDecimal("sum_total"),
                rs.getBigDecimal("vat")
        );
    }
}

