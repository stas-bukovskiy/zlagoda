package com.zlagoda.check;

import com.zlagoda.card.CustomerCard;
import com.zlagoda.employee.Employee;
import com.zlagoda.helpers.Entity;
import com.zlagoda.sale.Sale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Check implements Entity {

    private String checkNumber;
    private Employee employee;
    private CustomerCard customerCard;
    private Set<Sale> sales;
    private LocalDateTime printDate;
    private BigDecimal totalSum;
    private BigDecimal vat;

}
