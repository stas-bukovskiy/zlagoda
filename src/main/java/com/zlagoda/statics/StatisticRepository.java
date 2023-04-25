package com.zlagoda.statics;

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

    protected static final String COUNT_AVG_CHECK_FOR_TOP_5_CATEGORIES_SQL = """
            WITH variables (start_date, end_date) as (values (TO_TIMESTAMP('2023-04-01 08:00:00', 'YYYY-MM-DD HH24:MI:SS'),
                                                              TO_TIMESTAMP('2024-04-01 08:00:00', 'YYYY-MM-DD HH24:MI:SS')))
            SELECT Category.category_name, AVG("check".sum_total) AS avg_total
            FROM variables, Category
                     INNER JOIN Product ON Category.category_number = Product.category_number
                     INNER JOIN Store_Product ON Product.id_product = Store_Product.id_product
                     INNER JOIN Sale ON Store_Product.UPC = Sale.UPC
                     INNER JOIN "check" ON Sale.check_number = "check".check_number
            WHERE "check".print_date BETWEEN start_date AND end_date
            GROUP BY Category.category_name
            ORDER BY avg_total DESC
            LIMIT 5;
            """;

    public Map<String, BigDecimal> countAvgCheckForTop5Categories() {
        return jdbcTemplate.query(COUNT_AVG_CHECK_FOR_TOP_5_CATEGORIES_SQL, rse -> {
            Map<String, BigDecimal> result = new HashMap<>();
            while (rse.next()) {
                result.put(rse.getString("category_name"), rse.getBigDecimal("avg_total"));
            }
            return result;
        });
    }

    protected static final String FIND_CASHIERS_THAT_NOT_SOLD_PRODUCTS_WITH_CATEGORY_ID_SQL = """
            SELECT *
            FROM Employee e
            WHERE e.empl_role = 'CASHIER'
                AND NOT EXISTS(
                    SELECT *
                    FROM "check" c
                    WHERE c.id_employee = e.id_employee
                      AND c.check_number NOT IN (SELECT s.check_number
                                                 FROM sale s
                                                          INNER JOIN store_product sp on sp.upc = s.upc
                                                          INNER JOIN "product" p2 on p2."id_product" = sp."id_product"
                                                 WHERE category_number = ?)
                );  
            """;

    public List<Employee> findCashiersThatNotSoldProductsWithCategory(Long categoryId) {
        return jdbcTemplate.query(FIND_CASHIERS_THAT_NOT_SOLD_PRODUCTS_WITH_CATEGORY_ID_SQL,
                employeeRowMapper, categoryId);
    }


}
