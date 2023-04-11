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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto implements DTO {

    private String id;

    @NotNull(message = "username can not be null")
    @Size(min = 3, max = 100, message = "username must be between 3 and 100 characters")
    private String username;

    @Nullable
    @Size(max = 50, message = "password must be between 6 and 50 characters")
    private String password;

    @NotNull(message = "surname can not be null")
    @Size(max = 50, message = "surname length cannot be larger than 50 characters")
    private String surname;

    @NotNull(message = "name can not be null")
    @Size(max = 50, message = "name length cannot be larger than 50 characters")
    private String name;

    @Nullable
    @Size(max = 50, message = "patronymic length cannot be larger than 50 characters")
    private String patronymic;

    @NotNull(message = "role can not be null")
    private String role;

    @NotNull(message = "salary can not be null")
    @Digits(integer = 13, fraction = 4)
    @DecimalMin(value = "0.0", message = "salary must be larger than 0.0")
    private BigDecimal salary;

    @NotNull(message = "date of birth can not be null")
    @BirthDate
    private String dateOfBirth;

    @NotNull(message = "date of start can not be null")
    private String dateOfStart;

    @NotNull(message = "phone number can not be null")
    @Pattern(regexp = "^\\+?\\d{10,13}$", message = "phone number can be as example: '+380123456789")
    private String phoneNumber;

    @NotNull(message = "city can not be null")
    @Size(max = 50, message = "city length cannot be larger than 50 characters")
    private String city;

    @NotNull(message = "street can not be null")
    @Size(max = 50, message = "street length cannot be larger than 50 characters")
    private String street;

    @NotNull(message = "zip code can not be null")
    @Size(max = 9, message = "zip code length cannot be larger than 9 characters")
    private String zipCode;

}
