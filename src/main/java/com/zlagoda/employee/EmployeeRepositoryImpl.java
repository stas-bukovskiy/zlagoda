package com.zlagoda.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.zlagoda.utils.SortUtils.sortToString;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EmployeeRowMapper rowMapper;
    private final EmployeeIdGenerator idGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Employee> findAll(Sort sort) {
        String sql = "SELECT * " +
                "FROM employee " +
                "ORDER BY " + sortToString(sort);
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Employee> findById(String id) {
        String sql = "SELECT * " +
                "FROM employee " +
                "WHERE id_employee = ?";
        List<Employee> employees = jdbcTemplate.query(sql, rowMapper, id);
        return employees.isEmpty() ? Optional.empty() : Optional.of(employees.get(0));
    }

    @Override
    public Employee save(Employee employee) {
        employee.setId(idGenerator.generate());
        String sql = "INSERT INTO employee (id_employee, empl_username, empl_password, empl_surname, empl_name, empl_patronymic, empl_role, salary, date_of_birth, date_of_start, phone_number, city, street, zip_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                employee.getId(),
                employee.getUsername(),
                passwordEncoder.encode(employee.getPassword()),
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
    public Employee update(String s, Employee employee) {
        String sql = "UPDATE employee " +
                "SET empl_username=?, empl_surname=?, empl_name=?, empl_patronymic=?, empl_role=?, salary=?, date_of_birth=?, date_of_start=?, phone_number=?, city=?, street=?, zip_code=? " +
                "WHERE id_employee=?";
        jdbcTemplate.update(sql,
                employee.getUsername(),
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
                employee.getZipCode(),
                employee.getId());
        return employee;
    }

    @Override
    public Optional<Employee> deleteById(String id) {
        Optional<Employee> employeeOptional = findById(id);
        if (employeeOptional.isPresent()) {
            String sql = "DELETE FROM employee WHERE id_employee = ?";
            jdbcTemplate.update(sql, id);
        }
        return employeeOptional;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM employee";
        jdbcTemplate.update(sql);
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM employee WHERE empl_username = ?";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, username);
            return count > 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public Optional<Employee> findByUsername(String username) {
        String sql = "SELECT * FROM employee WHERE empl_username = ?";
        List<Employee> employees = jdbcTemplate.query(sql, rowMapper, username);
        return employees.isEmpty() ? Optional.empty() : Optional.of(employees.get(0));
    }

    @Override
    public void updatePasswordById(String id, String password) {
        String sql = "UPDATE employee " +
                "SET empl_password=?" +
                "WHERE id_employee=?";
        jdbcTemplate.update(sql, passwordEncoder.encode(password), id);
    }

    @Override
    public boolean existsById(String id) {
        String sql = "SELECT COUNT(*) FROM employee WHERE id_employee = ?";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, id);
            return count > 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public List<String> findAllDistinctCities() {
        String sql = "SELECT DISTINCT city FROM employee";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<String> findAllDistinctStreets() {
        String sql = "SELECT DISTINCT street FROM employee";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<String> findAllDistinctZipCodes() {
        String sql = "SELECT DISTINCT zip_code FROM employee";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public boolean existsByUsernameAndIdIsNot(String username, String id) {
        String sql = "SELECT COUNT(*) " +
                "FROM employee " +
                "WHERE empl_username = ? AND id_employee != ?";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, username, id);
            return count > 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public List<Employee> findAllByRole(EmployeeRole role) {
        String sql = "SELECT * " +
                "FROM employee " +
                "WHERE empl_role=?";
        return jdbcTemplate.query(sql, rowMapper, role.name());
    }
}
