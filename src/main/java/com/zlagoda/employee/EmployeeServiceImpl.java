package com.zlagoda.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    @Override
    public List<Employee> getAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Employee getById(String id) {
        return repository.findById(id)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    @Override
    public Employee create(EmployeeDto employeeDto) {
        return repository.save(
                new Employee(
                        null,
                        employeeDto.getUsername(),
                        employeeDto.getPassword(),
                        employeeDto.getSurname(),
                        employeeDto.getName(),
                        employeeDto.getPatronymic(),
                        employeeDto.getRole(),
                        employeeDto.getSalary(),
                        employeeDto.getDateOfBirth(),
                        employeeDto.getDateOfStart(),
                        employeeDto.getPhoneNumber(),
                        employeeDto.getCity(),
                        employeeDto.getStreet(),
                        employeeDto.getZipCode()
                )
        );
    }

    @Override
    public Employee update(String id, EmployeeDto employeeDto) {
        Employee employeeToUpdate = getById(id);
        employeeToUpdate.setUsername(employeeDto.getUsername());
        employeeToUpdate.setPassword(employeeDto.getPassword());
        employeeToUpdate.setSurname(employeeDto.getSurname());
        employeeToUpdate.setName(employeeDto.getName());
        employeeToUpdate.setPatronymic(employeeDto.getPatronymic());
        employeeToUpdate.setRole(employeeDto.getRole());
        employeeToUpdate.setSalary(employeeDto.getSalary());
        employeeToUpdate.setDateOfBirth(employeeDto.getDateOfBirth());
        employeeToUpdate.setDateOfStart(employeeDto.getDateOfStart());
        employeeToUpdate.setPhoneNumber(employeeDto.getPhoneNumber());
        employeeToUpdate.setCity(employeeDto.getCity());
        employeeToUpdate.setStreet(employeeDto.getStreet());
        employeeToUpdate.setZipCode(employeeDto.getZipCode());
        return repository.save(employeeToUpdate);
    }

    @Override
    public Employee deleteById(String id) {
        return repository.deleteById(id)
                .orElseThrow(EmployeeNotFoundException::new);
    }
}
