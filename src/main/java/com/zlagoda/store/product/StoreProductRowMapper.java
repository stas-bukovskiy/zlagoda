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
        String promStoreProductUPC = rs.getString("UPC_prom");
        return new StoreProduct(
                rs.getString("UPC"),
                rs.getBigDecimal("selling_price"),
                rs.getInt("products_number"),
                rs.getBoolean("promotional_product"),
                Product.builder().id(rs.getLong("id_product")).build(),
                promStoreProductUPC == null ? null :
                        StoreProduct.builder()
                                .upc(rs.getString("UPC_prom"))
                                .build()
        );
    }
}