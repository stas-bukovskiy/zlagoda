package com.zlagoda.store.product;

import com.zlagoda.helpers.Service;

import java.math.BigDecimal;

public interface StoreProductService extends Service<String, StoreProductDto> {
    boolean isUniqueToCreate(String upc);

    boolean isUniqueToUpdate(String oldUpc, String newUpc);

    BigDecimal getPriceByUpc(String upc);
}
