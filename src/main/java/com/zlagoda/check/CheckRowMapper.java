package com.zlagoda.check;

import com.zlagoda.card.CustomerCardRowMapper;
import com.zlagoda.employee.EmployeeRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;

@Component
@RequiredArgsConstructor
public class CheckRowMapper implements RowMapper<Check> {

    private final EmployeeRowMapper employeeRowMapper;
    private final CustomerCardRowMapper customerCardRowMapper;

    @Override
    public Check mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Check(
                rs.getString("check_number"),
                employeeRowMapper.mapRow(rs, -1),
                customerCardRowMapper.mapRow(rs, -1),
                new LinkedHashSet<>(),
                rs.getTimestamp("print_date").toLocalDateTime(),
                rs.getBigDecimal("sum_total"),
                rs.getBigDecimal("vat")
        );
    }
}

