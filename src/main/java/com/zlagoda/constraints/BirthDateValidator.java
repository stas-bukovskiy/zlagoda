package com.zlagoda.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Calendar;
import java.util.Date;

import static com.zlagoda.utils.DateConverter.convertToDate;

public class BirthDateValidator implements ConstraintValidator<BirthDate, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        final Date dateToValidate = convertToDate(value);
        Calendar dateInCalendar = Calendar.getInstance();
        dateInCalendar.setTime(dateToValidate);

        return Calendar.getInstance().get(Calendar.YEAR) - dateInCalendar.get(Calendar.YEAR) >= 18;
    }
}