package com.zlagoda.employee;

import com.zlagoda.constraints.BirthDate;
import com.zlagoda.helpers.DTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
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
public class EmployeeDto implements DTO {

    @Max(10)
    private String id;

    @NotNull
    @Size(min = 3, max = 100)
    private String username;

    @NotNull
    @Size(min = 6, max = 50)
    private String password;

    @NotNull
    @Max(50)
    private String surname;

    @NotNull
    @Max(50)
    private String name;

    @Nullable
    @Max(50)
    private String patronymic;

    @NotNull
    private EmployeeRole role;

    @NotNull
    @Digits(integer = 13, fraction = 4)
    @DecimalMin("0.0")
    private BigDecimal salary;

    @NotNull
    @BirthDate
    private Date dateOfBirth;

    @NotNull
    private Date dateOfStart;

    @NotNull
    @Pattern(regexp = "^\\+?\\d{10,13}$\n")
    private String phoneNumber;

    @NotNull
    @Max(50)
    private String city;

    @NotNull
    @Max(50)
    private String street;

    @NotNull
    @Max(9)
    private String zipCode;

}
