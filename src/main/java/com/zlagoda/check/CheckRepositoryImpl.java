package com.zlagoda.check;

import com.zlagoda.card.CustomerCard;
import com.zlagoda.card.CustomerCardNotFoundException;
import com.zlagoda.card.CustomerCardRepository;
import com.zlagoda.employee.Employee;
import com.zlagoda.employee.EmployeeNotFoundException;
import com.zlagoda.employee.EmployeeRepository;
import com.zlagoda.exception.EntityLinkException;
import com.zlagoda.exception.NotFoundException;
import com.zlagoda.product.ProductRepository;
import com.zlagoda.sale.Sale;
import com.zlagoda.sale.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.zlagoda.utils.SortUtils.sortToString;

@Repository
@RequiredArgsConstructor
public class CheckRepositoryImpl implements CheckRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CheckRowMapper rowMapper;
    private final ProductRepository productRepository;
    private final EmployeeRepository employeeRepository;
    private final CheckIdGenerator idGenerator;
    private final CustomerCardRepository customerCardRepository;
    private final SaleRepository saleRepository;

    @Override
    public List<Check> findAll(Sort sort) {
        String sql = "SELECT * " +
                "FROM \"check\" " +
                "ORDER BY " + sortToString(sort);
        return jdbcTemplate.query(sql, rowMapper).stream()
                .map(this::linkNestedEntities)
                .toList();
    }

    @Override
    public Optional<Check> findById(String checkNumber) {
        String sql = "SELECT * " +
                "FROM \"check\" " +
                "WHERE check_number = ?";
        List<Check> checks = jdbcTemplate.query(sql, rowMapper, checkNumber);
        return checks.isEmpty() ? Optional.empty() : Optional.of(linkNestedEntities(checks.get(0)));
    }

    @Override
    public Check save(Check check) {
        check.setCheckNumber(idGenerator.generate());
        String sql = "INSERT INTO \"check\" (check_number, id_employee, card_number, print_date, sum_total, vat) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                check.getCheckNumber(),
                check.getEmployee().getId(),
                check.getCustomerCard() == null ? null : check.getCustomerCard().getCardNumber(),
                check.getPrintDate(),
                check.getTotalSum(),
                check.getVat());
        return check;
    }

    @Override
    public Check update(String checkNumber, Check check) {
        throw new UnsupportedOperationException("checks can not be updated");
    }

    @Override
    public Optional<Check> deleteById(String checkNumber) {
        Optional<Check> checkOptional = findById(checkNumber);
        if (checkOptional.isPresent()) {
            String sql = "DELETE FROM \"check\" WHERE check_number = ?";
            jdbcTemplate.update(sql, checkNumber);
        }
        return checkOptional;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM \"check\"";
        jdbcTemplate.update(sql);
    }

    private Check linkNestedEntities(Check check) {
        try {
            linkEmployee(check);
            linkCustomerCard(check);
            linkSales(check);
        } catch (NotFoundException e) {
            throw new EntityLinkException("cannot link nested entity to check", e);
        }
        return check;
    }

    private void linkEmployee(Check check) {
        String employeeId = check.getEmployee().getId();
        Employee employeeToLink = employeeRepository.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);
        check.setEmployee(employeeToLink);
    }

    private void linkCustomerCard(Check check) {
        String customerCardNumber = check.getCustomerCard().getCardNumber();
        if (customerCardNumber != null) {
            CustomerCard customerCardToLink = customerCardRepository.findById(customerCardNumber)
                    .orElseThrow(CustomerCardNotFoundException::new);
            check.setCustomerCard(customerCardToLink);
        }
    }

    private void linkSales(Check check) {
        List<Sale> salesToLink = saleRepository.findAllByCheckNumber(check.getCheckNumber());
        salesToLink.forEach(saleToLink -> check.getSales().add(saleToLink));
    }


    @Override
    public boolean existsById(String checkNumber) {
        String sql = "SELECT COUNT(*) FROM \"check\" WHERE check_number = ?";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, checkNumber);
            return count > 0;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
