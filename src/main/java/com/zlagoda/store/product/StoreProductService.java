package com.zlagoda.store.product;

import com.zlagoda.helpers.Service;

public interface StoreProductService extends Service<String, StoreProductDto> {
    boolean isUpcUnique(String upc);
}
