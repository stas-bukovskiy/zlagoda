package com.zlagoda.check;

import com.zlagoda.card.CustomerCard;
import com.zlagoda.employee.Employee;
import com.zlagoda.sale.Sale;
import com.zlagoda.sale.SaleRowMapper;
import com.zlagoda.store.product.StoreProduct;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class CheckRowMapper implements RowMapper<Check> {
    @Override
    public Check mapRow(ResultSet rs, int rowNum) throws SQLException {
        // з цим зв'язком трохи не розібрався
        Set<Sale> sales = new HashSet<>();

        return new Check(

                rs.getString("check_number"),
                Employee.builder().id(rs.getString("id_employee")).build(),
                CustomerCard.builder().cardNumber(rs.getString("card_number")).build(),
                // з цим зв'язком трохи не розібрався
                sales,
                rs.getDate("print_date"),
                rs.getBigDecimal("sum_total"),
                rs.getBigDecimal("vat")
        );
    }
}

