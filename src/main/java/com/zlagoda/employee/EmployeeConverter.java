package com.zlagoda.employee;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.zlagoda.utils.DateConverter.convertToDate;
import static com.zlagoda.utils.DateConverter.convertToDateString;

@Component
public class EmployeeConverter extends com.zlagoda.helpers.Converter<Employee, EmployeeDto> {

    public EmployeeConverter() {
        super(new ModelMapper(), Employee.class, EmployeeDto.class);

        mapper.createTypeMap(Employee.class, EmployeeDto.class)
                .addMappings(mapping -> {
                    mapping.using(ctx -> ctx.getSource() == null ? null : ((EmployeeRole) ctx.getSource()).name())
                            .map(Employee::getRole, EmployeeDto::setRole);
                    mapping.using(ctx -> ctx.getSource() == null ? null : convertToDateString((Date) ctx.getSource()))
                            .map(Employee::getDateOfBirth, EmployeeDto::setDateOfBirth);
                    mapping.using(ctx -> ctx.getSource() == null ? null : convertToDateString((Date) ctx.getSource()))
                            .map(Employee::getDateOfStart, EmployeeDto::setDateOfStart);
                    mapping.skip(Employee::getPassword, EmployeeDto::setPassword);
                });
        mapper.createTypeMap(EmployeeDto.class, Employee.class)
                .addMappings(mapping -> {
                    mapping.using(ctx -> ctx.getSource() == null ? null : EmployeeRole.valueOf((String) ctx.getSource()))
                            .map(EmployeeDto::getRole, Employee::setRole);
                    mapping.using(ctx -> ctx.getSource() == null ? null : convertToDate((String) ctx.getSource()))
                            .map(EmployeeDto::getDateOfBirth, Employee::setDateOfBirth);
                    mapping.using(ctx -> ctx.getSource() == null ? null : convertToDate((String) ctx.getSource()))
                            .map(EmployeeDto::getDateOfStart, Employee::setDateOfStart);
                });
    }

}
