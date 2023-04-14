package com.zlagoda.sale;

import com.zlagoda.check.Check;
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
    private Check check;
    private String productNumber; //?? why String
    private BigDecimal sellingPrice;

}
