package com.zlagoda.store.product;

import com.zlagoda.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class StoreProductRowMapper implements RowMapper<StoreProduct> {

    @Override
    public StoreProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
        String promStoreProductUPC = rs.getString("prom_upc");
        Product product = new Product(
                rs.getLong("id_product"),
                rs.getString("product_name"),
                rs.getString("characteristics"),
                rs.getLong("category_number")
        );

        return new StoreProduct(
                rs.getString("upc"),
                rs.getBigDecimal("selling_price"),
                rs.getInt("products_number"),
                rs.getBoolean("promotional_product"),
                product,
                promStoreProductUPC == null ? null :
                        new StoreProduct(
                                promStoreProductUPC,
                                rs.getBigDecimal("prom_selling_price"),
                                rs.getInt("prom_products_number"),
                                rs.getBoolean("prom_promotional_product"),
                                product,
                                null
                        )
        );
    }
}