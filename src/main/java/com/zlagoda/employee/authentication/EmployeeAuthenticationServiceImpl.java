package com.zlagoda.employee.authentication;

import com.zlagoda.employee.Employee;
import com.zlagoda.employee.EmployeeRole;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeAuthenticationServiceImpl implements EmployeeAuthenticationService {

    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Employee authenticate(String username, String password) throws AuthenticationException {
        String sql = "SELECT username, password, role FROM users WHERE username = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, username);
        if (rows.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        Map<String, Object> row = rows.get(0);
        String storedPassword = (String) row.get("password");
        if (!passwordEncoder.matches(password, storedPassword)) {
            throw new BadCredentialsException("Incorrect password");
        }

        return Employee.builder()
                .username((String) row.get("username"))
                .password(storedPassword)
                .role(EmployeeRole.valueOf((String) row.get("role")))
                .build();
    }
}
