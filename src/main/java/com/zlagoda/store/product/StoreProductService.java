package com.zlagoda.store.product;

import com.zlagoda.helpers.Service;

public interface StoreProductService extends Service<String, StoreProductDto> {
    boolean isUniqueToCreate(String upc);

    boolean isUniqueToUpdate(String oldUpc, String newUpc);
}
