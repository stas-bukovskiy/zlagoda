package com.zlagoda.store.product;

import com.zlagoda.helpers.Entity;
import com.zlagoda.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreProduct implements Entity {

    private String upc;
    private BigDecimal sellingPrice;
    private int productsNumber;
    private boolean isPromotional;
    private Product product;
    private StoreProduct promStoreProduct;

}
