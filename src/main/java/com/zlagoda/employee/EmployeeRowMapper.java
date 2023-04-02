package com.zlagoda.employee;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EmployeeRowMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Employee(
                rs.getString("id_employee"),
                rs.getString("empl_username"),
                rs.getString("empl_password"),
                rs.getString("empl_surname"),
                rs.getString("empl_name"),
                rs.getString("empl_patronymic"),
                EmployeeRole.valueOf(rs.getString("empl_role")),
                rs.getBigDecimal("salary"),
                rs.getDate("date_of_birth"),
                rs.getDate("date_of_start"),
                rs.getString("phone_number"),
                rs.getString("city"),
                rs.getString("street"),
                rs.getString("zip_code")
        );
    }
}