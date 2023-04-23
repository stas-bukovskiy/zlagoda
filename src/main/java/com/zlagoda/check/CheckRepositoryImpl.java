package com.zlagoda.check;

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
    private final CheckIdGenerator idGenerator;
    private final SaleRepository saleRepository;

    @Override
    public List<Check> findAll(Sort sort) {
        String sql = """
                SELECT *
                FROM "check"
                INNER JOIN employee e on e.id_employee = "check".id_employee
                LEFT JOIN customer_card cc on cc.card_number = "check".card_number
                ORDER BY ?
                 """;
        return jdbcTemplate.query(sql, rowMapper, sortToString(sort)).stream()
                .peek(this::linkSales)
                .toList();
    }

    @Override
    public Optional<Check> findById(String checkNumber) {
        String sql = """
                SELECT *
                FROM "check"
                INNER JOIN employee e on e.id_employee = "check".id_employee
                LEFT JOIN customer_card cc on cc.card_number = "check".card_number
                WHERE check_number = ?
                """;
        List<Check> checks = jdbcTemplate.query(sql, rowMapper, checkNumber);
        if (!checks.isEmpty()) {
            linkSales(checks.get(0));
            return Optional.of(checks.get(0));
        }
        return Optional.empty();
    }

    @Override
    public Check save(Check check) {
        check.setCheckNumber(idGenerator.generate());
        String sql = """
                INSERT INTO "check" (check_number,
                                    id_employee,
                                    card_number,
                                    print_date,
                                    sum_total,
                                    vat)
                      VALUES (?, ?, ?, ?, ?, ?)
                               """;
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
            String sql = """
                    DELETE
                    FROM "check"
                    WHERE check_number = ?
                    """;
            jdbcTemplate.update(sql, checkNumber);
        }
        return checkOptional;
    }

    @Override
    public void deleteAll() {
        String sql = """
                DELETE
                FROM "check"
                """;
        jdbcTemplate.update(sql);
    }

    private void linkSales(Check check) {
        List<Sale> salesToLink = saleRepository.findAllByCheckNumber(check.getCheckNumber());
        salesToLink.forEach(saleToLink -> check.getSales().add(saleToLink));
    }


    @Override
    public boolean existsById(String checkNumber) {
        String sql = """
                SELECT COUNT(*)
                FROM "check"
                WHERE check_number = ?
                 """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, checkNumber);
        if (count == null)
            return false;
        return count > 0;
    }

    @Override
    public List<Check> findAllEmployeeId(String id) {
        String sql = """
                SELECT *
                FROM "check"
                INNER JOIN employee e on e.id_employee = "check".id_employee
                LEFT JOIN customer_card cc on cc.card_number = "check".card_number
                WHERE e.id_employee = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, id).stream()
                .peek(this::linkSales)
                .toList();
    }

    @Override
    public void deleteAllByEmployeeId(String employeeId) {
        String sql = """
                DELETE
                FROM "check"
                WHERE id_employee = ?
                """;
        jdbcTemplate.update(sql, employeeId);
    }

    @Override
    public void deleteByCustomerCardId(String cardNumber) {
        String sql = """
                DELETE
                FROM "check"
                WHERE card_number = ?
                """;
        jdbcTemplate.update(sql, cardNumber);
    }

    @Override
    public List<Check> findByCardNumber(String cardNumber) {
        String sql = """
                SELECT *
                FROM "check"
                INNER JOIN employee e on e.id_employee = "check".id_employee
                LEFT JOIN customer_card cc on cc.card_number = "check".card_number
                WHERE "check".card_number = ?
                 """;
        return jdbcTemplate.query(sql, rowMapper, cardNumber).stream()
                .peek(this::linkSales)
                .toList();
    }
}
