package com.zlagoda.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EmployeeRowMapper rowMapper;
    private final EmployeeIdGenerator idGenerator;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Employee> findAll(Sort sort) {
        String sql = "SELECT * FROM employee ORDER BY " + sort.toString();
        return jdbcTemplate.query(sql, new EmployeeRowMapper());
    }

    @Override
    public Optional<Employee> findById(String id) {
        String sql = "SELECT * FROM employee WHERE id = ?";
        List<Employee> employees = jdbcTemplate.query(sql, new Object[]{id}, rowMapper);
        return employees.isEmpty() ? Optional.empty() : Optional.of(employees.get(0));
    }

    @Override
    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(idGenerator.generate());
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        }
        String sql = "INSERT INTO employee (id, username, password, surname, name, patronymic, role, salary, date_of_birth, date_of_start, phone_number, city, street, zip_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                employee.getId(),
                employee.getUsername(),
                employee.getPassword(),
                employee.getSurname(),
                employee.getName(),
                employee.getPatronymic(),
                employee.getRole().name(),
                employee.getSalary(),
                employee.getDateOfBirth(),
                employee.getDateOfStart(),
                employee.getPhoneNumber(),
                employee.getCity(),
                employee.getStreet(),
                employee.getZipCode());
        return employee;
    }

    @Override
    public Optional<Employee> deleteById(String id) {
        Optional<Employee> employeeOptional = findById(id);
        if (employeeOptional.isPresent()) {
            String sql = "DELETE FROM employee WHERE id = ?";
            jdbcTemplate.update(sql, id);
        }
        return employeeOptional;
    }
}
