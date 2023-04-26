package com.zlagoda.statistic;

import com.zlagoda.employee.EmployeeConverter;
import com.zlagoda.employee.EmployeeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticRepository repository;
    private final EmployeeConverter employeeConverter;


    public Map<EmployeeDto, Pair<Long, BigDecimal>> countTotalSoldAndSales() {
        return repository.countTotalSoldAndSales().entrySet().stream()
                .map(employeePairEntry ->
                        Map.entry(
                                employeeConverter.convertToDto(employeePairEntry.getKey()),
                                employeePairEntry.getValue())
                ).collect(
                        Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue,
                                (v1, v2) -> v1,
                                HashMap::new
                        )
                );

    }

    public List<EmployeeDto> findCashiersWithoutNotPromoProductSales() {
        return repository.findCashiersWithoutNotPromoProductSales().stream()
                .map(employeeConverter::convertToDto)
                .toList();
    }

    public List<EmployeeDto> findCashiersWithoutPromoProductSales() {
        return repository.findCashiersWithoutPromoProductSales().stream()
                .map(employeeConverter::convertToDto)
                .toList();
    }

}
