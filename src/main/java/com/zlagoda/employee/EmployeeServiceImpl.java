package com.zlagoda.employee;

import com.zlagoda.check.CheckService;
import com.zlagoda.confiramtion.DeleteConfirmation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    public final static Sort DEFAULT_SORT = Sort.by("empl_name");

    private final EmployeeRepository repository;
    private final EmployeeConverter converter;
    private final CheckService checkService;

    @Override
    public List<EmployeeDto> getAll(Sort sort) {
        return repository.findAll(sort)
                .stream()
                .map(converter::convertToDto)
                .toList();
    }

    @Override
    public EmployeeDto getById(String id) {
        return repository.findById(id)
                .map(converter::convertToDto)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    @Override
    public EmployeeDto create(EmployeeDto employeeDto) {
        Employee employeeToCreate = converter.convertToEntity(employeeDto);
        Employee createdEmployee = repository.save(employeeToCreate);
        return converter.convertToDto(createdEmployee);
    }

    @Override
    public EmployeeDto update(String id, EmployeeDto employeeDto) {
        if (!repository.existsById(id))
            throw new EmployeeNotFoundException();
        Employee employeeToUpdate = converter.convertToEntity(employeeDto);
        if (employeeDto.getPassword() != null && !employeeDto.getPassword().equals(""))
            repository.updatePasswordById(id, employeeToUpdate.getPassword());
        Employee updatedEmployee = repository.update(id, employeeToUpdate);
        return converter.convertToDto(updatedEmployee);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EmployeeDto deleteById(String id) {
        EmployeeDto employeeToDelete = getById(id);
        checkService.deleteAllByEmployeeId(id);
        repository.deleteById(id);
        return employeeToDelete;
    }

    @Override
    public List<Employee> getAllCashiers() {
        return repository.findAllByRole(EmployeeRole.CASHIER);
    }

    @Override
    public List<EmployeeRole> getRoles() {
        return Arrays.stream(EmployeeRole.values())
                .toList();
    }

    @Override
    public List<String> getCities() {
        return repository.findAllDistinctCities();
    }

    @Override
    public List<String> getStreets() {
        return repository.findAllDistinctStreets();
    }

    @Override
    public List<String> getZipCodes() {
        return repository.findAllDistinctZipCodes();
    }

    @Override
    public boolean isUsernameUniqueToCreate(String username) {
        return !repository.existsByUsername(username);
    }

    @Override
    public boolean isUsernameUniqueToUpdate(String id, String username) {
        return !repository.existsByUsernameAndIdIsNot(username, id);
    }

    @Override
    public DeleteConfirmation createDeleteConfirmation(String id) {
        DeleteConfirmation confirmation = new DeleteConfirmation();
        confirmation.setId(id);
        confirmation.setObjectName("Employee");
        List<DeleteConfirmation> childRemovals = new ArrayList<>(checkService.createChildDeleteConfirmation(id));
        confirmation.setChildRemovals(childRemovals);
        return confirmation;
    }
}
