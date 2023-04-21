package com.zlagoda.check;

import com.zlagoda.employee.EmployeeService;
import com.zlagoda.sale.SaleDto;
import com.zlagoda.sale.SaleService;
import com.zlagoda.store.product.StoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckServiceImpl implements CheckService {

    public final static Sort DEFAULT_SORT = Sort.by("check_number");

    private final CheckProperties properties;
    private final CheckConverter converter;
    private final CheckRepository repository;
    private final StoreProductService storeProductService;
    private final SaleService saleService;
    private final EmployeeService employeeService;


    @Override
    public List<CheckDto> getAll(Sort sort) {
        return repository.findAll(sort)
                .stream()
                .map(converter::convertToDto)
                .toList();
    }

    @Override
    public CheckDto getById(String checkNumber) {
        return repository.findById(checkNumber)
                .map(converter::convertToDto)
                .orElseThrow(CheckNotFoundException::new);
    }

    @Override
    public CheckDto create(CheckDto checkDto) {
        Check checkToCreate = converter.convertToEntity(checkDto);

        checkToCreate.setEmployee(employeeService.getCurrentUser());
        checkToCreate.setPrintDate(new Date());

        BigDecimal sum = countSalesSum(checkDto);
        BigDecimal vat = countVat(sum);
        checkToCreate.setTotalSum(sum.add(vat));
        checkToCreate.setVat(vat);

        Check createdCheck = repository.save(checkToCreate);
        List<SaleDto> createdSales = checkDto.getSales().stream()
                .peek(saleDto -> saleDto.setCheckNumber(createdCheck.getCheckNumber()))
                .map(saleService::create)
                .toList();
        CheckDto createdCheckDto = converter.convertToDto(createdCheck);
        createdCheckDto.setSales(createdSales);
        return createdCheckDto;
    }

    @Override
    public CheckDto update(String checkNumber, CheckDto checkDto) {
        throw new UnsupportedOperationException("checks can not be updated");
    }

    @Override
    public CheckDto deleteById(String checkNumber) {
        return repository.deleteById(checkNumber)
                .map(converter::convertToDto)
                .orElseThrow(CheckNotFoundException::new);
    }

    @Override
    public BigDecimal countSalesSum(CheckDto checkDto) {
        return checkDto.getSales().stream()
                .map(sale -> storeProductService.getPriceByUpc(sale.getStoreProductUpc())
                        .multiply(BigDecimal.valueOf(sale.getProductNumber()))
                ).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(4, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal countVat(BigDecimal totalSum) {
        return totalSum.multiply(properties.getVat()).setScale(4, RoundingMode.HALF_UP);
    }

}
