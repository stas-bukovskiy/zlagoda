package com.zlagoda.shop.product;

import com.zlagoda.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class ShopProduct {

    private String upc;
    private BigDecimal sellingPrice;
    private int productsNumber;
    private boolean isPromotional;
    private Product product;
    private ShopProduct promShopProduct;

}
