package com.zlagoda.sale;

import com.zlagoda.shop.product.ShopProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class Sale {

    private ShopProduct shopProduct;
    private String productNumber;
    private BigDecimal sellingPrice;

}
