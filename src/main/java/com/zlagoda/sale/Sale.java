package com.zlagoda.sale;

import com.zlagoda.helpers.Entity;
import com.zlagoda.store.product.StoreProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sale implements Entity {

    private StoreProduct storeProduct;
    private String checkNumber;
    private int productNumber;
    private BigDecimal sellingPrice;

}
