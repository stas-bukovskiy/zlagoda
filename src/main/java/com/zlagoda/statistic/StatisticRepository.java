package com.zlagoda.statistic;

import com.zlagoda.employee.Employee;
import com.zlagoda.employee.EmployeeRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class StatisticRepository {

    protected static final String COUNT_TOTAL_SOLD_AND_SALES_SQL = """
            WITH variables (start_date, end_date) as (VALUES (TO_TIMESTAMP('2023-04-25 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
                                                              TO_TIMESTAMP('2023-04-26 00:00:00', 'YYYY-MM-DD HH24:MI:SS')))
            SELECT e.id_employee, e.empl_name, e.empl_surname, SUM(S.product_number) AS total_sold, SUM(s.product_number * s.selling_price) AS total_sales
            FROM variables, employee e
                     JOIN "check" c ON e.id_employee = c.id_employee
                     JOIN sale s ON c.check_number = s.check_number
            WHERE c.print_date BETWEEN start_date AND end_date
            GROUP BY e.empl_name, e.empl_surname, e.id_employee;
            """;
    protected static final String FIND_CASHIERS_WITHOUT_NOT_PROMO_PRODUCTS_SQL = """
            SELECT *
            FROM employee
            WHERE empl_role = 'CASHIER'
              AND id_employee NOT IN (SELECT id_employee
                                      FROM "check"
                                      WHERE check_number NOT IN (SELECT DISTINCT s.check_number
                                                                 FROM sale s
                                                                          INNER JOIN store_product sp ON s.upc = sp.upc
                                                                 WHERE sp.promotional_product != ?
                                                                )
                                    );
            """;
    private final JdbcTemplate jdbcTemplate;
    private final EmployeeRowMapper employeeRowMapper;

    public Map<Employee, Pair<Long, BigDecimal>> countTotalSoldAndSales() {
        return jdbcTemplate.query(COUNT_TOTAL_SOLD_AND_SALES_SQL, rse -> {
            Map<Employee, Pair<Long, BigDecimal>> result = new HashMap<>();
            while (rse.next()) {
                result.put(new Employee(
                                rse.getString("id_employee"),
                                null, null,
                                rse.getString("empl_surname"),
                                rse.getString("empl_name"),
                                null, null, null, null, null,
                                null, null, null, null),
                        Pair.of(rse.getLong("total_sold"), rse.getBigDecimal("total_sales")));
            }
            return result;
        });
    }

    public List<Employee> findCashiersWithoutNotPromoProductSales() {
        return jdbcTemplate.query(FIND_CASHIERS_WITHOUT_NOT_PROMO_PRODUCTS_SQL, employeeRowMapper, false);
    }

    public List<Employee> findCashiersWithoutPromoProductSales() {
        return jdbcTemplate.query(FIND_CASHIERS_WITHOUT_NOT_PROMO_PRODUCTS_SQL, employeeRowMapper, true);
    }


}
