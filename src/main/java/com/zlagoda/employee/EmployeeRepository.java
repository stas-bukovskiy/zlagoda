package com.zlagoda.employee;

import com.zlagoda.helpers.Repository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends Repository<Employee, String> {
    Optional<Employee> findByUsername(String username);

    void updatePasswordById(String id, String password);

    boolean existsById(String id);

    List<String> findAllDistinctCities();

    List<String> findAllDistinctStreets();

    List<String> findAllDistinctZipCodes();
}
