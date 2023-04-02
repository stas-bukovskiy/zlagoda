package com.zlagoda.employee;

import com.zlagoda.exception.NotFoundException;

public class EmployeeNotFoundException extends NotFoundException {
    public EmployeeNotFoundException() {
        super("Not found such employee");
    }
}
