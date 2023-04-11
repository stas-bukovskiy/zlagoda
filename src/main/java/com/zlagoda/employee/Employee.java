package com.zlagoda.employee;

import com.zlagoda.helpers.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Entity {

    private String id;
    private String username;
    private String password;
    private String surname;
    private String name;
    private String patronymic;
    private EmployeeRole role;
    private BigDecimal salary;
    private Date dateOfBirth;
    private Date dateOfStart;
    private String phoneNumber;
    private String city;
    private String street;
    private String zipCode;

}
