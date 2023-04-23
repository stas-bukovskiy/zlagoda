package com.zlagoda.check;

import com.zlagoda.helpers.Repository;

import java.util.List;

public interface CheckRepository extends Repository<Check, String> {

    boolean existsById(String checkNumber);

    List<Check> findAllEmployeeId(String id);

    void deleteAllByEmployeeId(String employeeId);

    void deleteByCustomerCardId(String cardNumber);

    List<Check> findByCardNumber(String cardNumber);
}
