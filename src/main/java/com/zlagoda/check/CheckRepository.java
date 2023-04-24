package com.zlagoda.check;

import com.zlagoda.helpers.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface CheckRepository extends Repository<Check, String> {

    BigDecimal countTotalSumByEmployeeId(String employeeId, Timestamp from, Timestamp to);

    BigDecimal countTotalSum(Timestamp from, Timestamp to);

    Long countTotalAmountSoldByProductId(Long productId, Timestamp from, Timestamp to);

    boolean existsById(String checkNumber);

    List<Check> findAllEmployeeId(String id);

    void deleteAllByEmployeeId(String employeeId);

    void deleteByCustomerCardId(String cardNumber);

    List<Check> findByCardNumber(String cardNumber);

    List<Check> findAllByEmployeeIdAndForCurrentDay(String employeeId);

    List<Check> findAllAndEmployeeIdByPrintDateBetween(String employeeId, Timestamp from, Timestamp to);
}
