package com.zlagoda.employee.authentication;

import com.zlagoda.employee.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeDetailsServiceImpl implements UserDetailsService {

    private final EmployeeAuthenticationService employeeAuthenticationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeAuthenticationService.authenticate(username, null);
        if (employee == null) {
            throw new UsernameNotFoundException("Employee not found");
        }
        return new EmployeePrincipal(employee);
    }
}