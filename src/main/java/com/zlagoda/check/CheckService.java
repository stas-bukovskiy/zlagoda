package com.zlagoda.check;

import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.helpers.Service;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.util.List;

public interface CheckService extends Service<String, CheckDto> {

    BigDecimal countSalesSum(CheckDto checkDto);

    BigDecimal countVat(BigDecimal totalSum);

    List<FieldError> checkAvailability(CheckDto checkDto);

    List<DeleteConfirmation> createChildDeleteConfirmation(String employeeId);

    void deleteAllByEmployeeId(String id);
}
