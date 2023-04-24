package com.zlagoda.check;

import com.zlagoda.sale.Sale;
import com.zlagoda.sale.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CheckRepositoryImpl implements CheckRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CheckRowMapper rowMapper;
    private final CheckIdGenerator idGenerator;
    private final SaleRepository saleRepository;

    @Override
    public List<Check> findAll() {
        String sql = """
                SELECT *
                FROM "check"
                INNER JOIN employee e on e.id_employee = "check".id_employee
                LEFT JOIN customer_card cc on cc.card_number = "check".card_number
                 """;
        return jdbcTemplate.query(sql, rowMapper).stream().peek(this::linkSales).toList();
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
        jdbcTemplate.update(sql, check.getCheckNumber(), check.getEmployee().getId(), check.getCustomerCard() == null ? null : check.getCustomerCard().getCardNumber(), check.getPrintDate(), check.getTotalSum(), check.getVat());
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
    public BigDecimal countTotalSumByEmployeeId(String employeeId, Timestamp from, Timestamp to) {
        String sql = """
                SELECT SUM(s.selling_price * s.product_number) AS total_sales
                FROM Sale s
                INNER JOIN "check" c ON s.check_number = c.check_number
                WHERE c.id_employee = ? AND c.id_employee BETWEEN ? AND  ?;
                """;
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class, employeeId, from, to);
        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal countTotalSum(Timestamp from, Timestamp to) {
        String sql = """
                SELECT SUM(s.selling_price * s.product_number * 1.2) AS total_sales
                FROM Sale s
                INNER JOIN "check" c ON s.check_number = c.check_number
                WHERE c.print_date BETWEEN ? AND ?
                """;
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class, from, to);
        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public Long countTotalAmountSoldByProductId(Long productId, Timestamp from, Timestamp to) {
        String sql = """
                SELECT SUM(s.product_number) AS total_units_sold
                FROM sale s
                INNER JOIN "check" c ON s.check_number = c.check_number
                INNER JOIN store_product sp ON s.UPC = sp.UPC
                WHERE sp.id_product = ? AND c.print_date BETWEEN ? AND ?
                """;
        Long result = jdbcTemplate.queryForObject(sql, Long.class, productId, from, to);
        return result == null ? 0 : result;
    }

    @Override
    public boolean existsById(String checkNumber) {
        String sql = """
                SELECT COUNT(*)
                FROM "check"
                WHERE check_number = ?
                 """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, checkNumber);
        if (count == null) return false;
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
        return jdbcTemplate.query(sql, rowMapper, id).stream().peek(this::linkSales).toList();
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
        return jdbcTemplate.query(sql, rowMapper, cardNumber).stream().peek(this::linkSales).toList();
    }

    @Override
    public List<Check> findAllByEmployeeIdAndForCurrentDay(String employeeId) {
        String sql = """
                SELECT *
                FROM "check"
                INNER JOIN employee e on e.id_employee = "check".id_employee
                LEFT JOIN customer_card cc on cc.card_number = "check".card_number
                WHERE "check".print_date >= current_date
                AND "check".print_date < current_date + INTERVAL '1' DAY
                AND "check".id_employee = ?;
                 """;
        return jdbcTemplate.query(sql, rowMapper, employeeId).stream().peek(this::linkSales).toList();
    }

    @Override
    public List<Check> findAllAndEmployeeIdByPrintDateBetween(String employeeId, Timestamp from, Timestamp to) {
        String sql = """
                SELECT *
                FROM "check"
                INNER JOIN employee e on e.id_employee = "check".id_employee
                LEFT JOIN customer_card cc on cc.card_number = "check".card_number
                WHERE "check".print_date BETWEEN ? AND ?
                AND "check".id_employee = ?;
                 """;
        return jdbcTemplate.query(sql, rowMapper, from, to, employeeId).stream().peek(this::linkSales).toList();
    }
}
