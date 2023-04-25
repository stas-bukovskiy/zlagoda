package com.zlagoda.check;

import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.helpers.Service;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface CheckService extends Service<String, CheckDto> {

    List<CheckDto> getAllByEmployeeId(String employeeId);

    BigDecimal countTotalSumByEmployeeId(String employeeId, Timestamp from, Timestamp to);

    BigDecimal countTotalSum(Timestamp from, Timestamp to);

    Long countTotalAmountSoldByProductId(Long productId, Timestamp from, Timestamp to);

    BigDecimal countSalesSum(CheckDto checkDto);

    BigDecimal countVat(BigDecimal totalSum);

    List<FieldError> checkAvailability(CheckDto checkDto);

    List<DeleteConfirmation> createChildDeleteConfirmation(String employeeId);

    void deleteAllByEmployeeId(String id);

    List<CheckDto> getTodayChecksOfCurrentUser();

    List<CheckDto> getAllByPrintDateBetween(Timestamp from, Timestamp to);
}
