package com.zlagoda.sale;

import com.zlagoda.store.product.StoreProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class Sale {

    private StoreProduct shopProduct;
    private String productNumber;
    private BigDecimal sellingPrice;

}
