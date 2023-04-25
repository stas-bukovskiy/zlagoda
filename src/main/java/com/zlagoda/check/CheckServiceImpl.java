package com.zlagoda.check;

import com.zlagoda.card.CustomerCard;
import com.zlagoda.card.CustomerCardService;
import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.employee.Employee;
import com.zlagoda.employee.authentication.EmployeePrincipal;
import com.zlagoda.sale.SaleDto;
import com.zlagoda.sale.SaleService;
import com.zlagoda.store.product.StoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckServiceImpl implements CheckService {

    private final CheckProperties properties;
    private final CheckConverter converter;
    private final CheckRepository repository;
    private final CustomerCardService customerCardService;
    private final StoreProductService storeProductService;
    private final SaleService saleService;


    @Override
    public List<CheckDto> getAll() {
        return repository.findAll()
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public CheckDto create(CheckDto checkDto) {
        Check checkToCreate = converter.convertToEntity(checkDto);

        checkToCreate.setEmployee(getCurrentUser());
        checkToCreate.setPrintDate(LocalDateTime.now());

        BigDecimal sum = countSalesSum(checkDto);
        BigDecimal vat = countVat(sum);
        sum = sum.add(vat);
        String customerCardNumber = checkDto.getCardNumber();
        if (!customerCardNumber.equals("")) {
            checkToCreate.setCustomerCard(CustomerCard.builder().cardNumber(customerCardNumber).build());
            double percent = customerCardService.getById(customerCardNumber).getPercent();
            sum = sum.multiply(BigDecimal.valueOf(1 - percent / 100));
        }
        checkToCreate.setTotalSum(sum);
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

    @Override
    public List<FieldError> checkAvailability(CheckDto checkDto) {
        List<FieldError> fieldErrors = new ArrayList<>();
        checkDto.getSales().forEach(saleDto -> {
            int availableAMMount = storeProductService.getAmountByUpc(saleDto.getStoreProductUpc());
            if (saleDto.getProductNumber() > availableAMMount) {
                fieldErrors.add(new FieldError(
                        "check", "" +
                                 "sales", "" +
                                          "product with upc '" + saleDto.getStoreProductUpc() + "' max amount is " +
                                          availableAMMount + ", but specified: " + saleDto.getProductNumber()
                ));
            }
        });
        return fieldErrors;
    }

    @Override
    public List<DeleteConfirmation> createChildDeleteConfirmation(String employeeId) {
        return repository.findAllEmployeeId(employeeId).stream()
                .map(check -> new DeleteConfirmation(check.getCheckNumber(), "Check", null))
                .toList();
    }

    @Override
    public void deleteAllByEmployeeId(String employeeId) {
        repository.deleteAllByEmployeeId(employeeId);
    }

    @Override
    public List<CheckDto> getTodayChecksOfCurrentUser() {
        Employee currentUser = getCurrentUser();
        return repository.findAllByEmployeeIdAndForCurrentDay(currentUser.getId()).stream()
                .map(converter::convertToDto)
                .toList();
    }

    @Override
    public List<CheckDto> getAllByPrintDateBetween(Timestamp from, Timestamp to) {
        Employee currentUser = getCurrentUser();
        return repository.findAllAndEmployeeIdByPrintDateBetween(currentUser.getId(), from, to).stream()
                .map(converter::convertToDto)
                .toList();
    }

    @Override
    public List<CheckDto> getAllByEmployeeId(String employeeId) {
        return repository.findAllEmployeeId(employeeId).stream()
                .map(converter::convertToDto)
                .toList();
    }

    @Override
    public BigDecimal countTotalSumByEmployeeId(String employeeId, Timestamp from, Timestamp to) {
        return repository.countTotalSumByEmployeeId(employeeId, from, to);
    }

    @Override
    public BigDecimal countTotalSum(Timestamp from, Timestamp to) {
        return repository.countTotalSum(from, to);
    }

    @Override
    public Long countTotalAmountSoldByProductId(Long productId, Timestamp from, Timestamp to) {
        return repository.countTotalAmountSoldByProductId(productId, from, to);
    }

    public Employee getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeePrincipal employeePrincipal = (EmployeePrincipal) authentication.getPrincipal();
        return employeePrincipal.getEmployee();
    }

}
