package com.zlagoda.employee.authentication;

import com.zlagoda.employee.Employee;
import org.springframework.security.core.AuthenticationException;

public interface EmployeeAuthenticationService {
    Employee authenticate(String username, String password) throws AuthenticationException;
}