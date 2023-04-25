package com.zlagoda.statistic;

import com.zlagoda.employee.Employee;
import com.zlagoda.employee.EmployeeRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class StatisticRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EmployeeRowMapper employeeRowMapper;


    protected static final String COUNT_MOST_SALABLE_PRODUCTS_SQL = """
            WITH variables (start_date, end_date) as (values (TO_TIMESTAMP('2023-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
                                                              TO_TIMESTAMP('2023-04-27 23:59:59', 'YYYY-MM-DD HH24:MI:SS')))
            SELECT Product.product_name, SUM(Sale.product_number) AS total_sales
            FROM variables, Product
                     INNER JOIN Store_Product ON Product.id_product = Store_Product.id_product
                     INNER JOIN Sale ON Store_Product.UPC = Sale.UPC
                     INNER JOIN "check" ON Sale.check_number = "check".check_number
            WHERE "check".print_date BETWEEN start_date AND end_date
            GROUP BY Product.product_name
            ORDER BY total_sales DESC
            LIMIT 10;
            """;

    public Map<String, BigDecimal> countMostSalableProducts() {
        return jdbcTemplate.query(COUNT_MOST_SALABLE_PRODUCTS_SQL, rse -> {
            Map<String, BigDecimal> result = new HashMap<>();
            while (rse.next()) {
                result.put(rse.getString("product_name"), rse.getBigDecimal("total_sales"));
            }
            return result;
        });
    }

    protected static final String FIND_CASHIERS_WITHOUT_SALES_SQL = """
            SELECT *
            FROM Employee e
            WHERE e.empl_role = 'CASHIER'
              AND NOT EXISTS(
                    SELECT *
                    FROM "check" c
                    WHERE c.id_employee = e.id_employee
                      AND NOT EXISTS(
                            SELECT *
                            FROM Sale s
                            WHERE s.check_number = c.check_number
                        )
                );
            """;

    public List<Employee> findCashiersWithoutSales() {
        return jdbcTemplate.query(FIND_CASHIERS_WITHOUT_SALES_SQL, employeeRowMapper);
    }

}
