package com.zlagoda.check;

import com.zlagoda.card.Card;
import com.zlagoda.employee.Employee;
import com.zlagoda.sale.Sale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
public class Check {

    private String checkNumber;
    private Employee employee;
    private Card customerCard;
    private Set<Sale> sales;
    private Date printDate;
    private BigDecimal totalSum;
    private BigDecimal vat;

}
