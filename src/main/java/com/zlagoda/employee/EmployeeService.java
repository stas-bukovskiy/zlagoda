package com.zlagoda.employee;

import com.zlagoda.helpers.Service;

import java.util.List;

public interface EmployeeService extends Service<String, EmployeeDto> {
    List<EmployeeRole> getRoles();

    List<String> getCities();

    List<String> getStreets();

    List<String> getZipCodes();

    boolean isUsernameUniqueToCreate(String username);

    boolean isUsernameUniqueToUpdate(String id, String username);
}
