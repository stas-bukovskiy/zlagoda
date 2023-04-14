package com.zlagoda.store.product;

import com.zlagoda.exception.NotFoundException;

public class StoreProductNotFoundException extends NotFoundException {
    public StoreProductNotFoundException() {
        super("Not found such store product");
    }
}
