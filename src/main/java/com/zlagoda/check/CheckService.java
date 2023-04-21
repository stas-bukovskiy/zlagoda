package com.zlagoda.check;

import com.zlagoda.helpers.Service;

import java.math.BigDecimal;

public interface CheckService extends Service<String, CheckDto> {

    BigDecimal countSalesSum(CheckDto checkDto);

    BigDecimal countVat(BigDecimal totalSum);
}
