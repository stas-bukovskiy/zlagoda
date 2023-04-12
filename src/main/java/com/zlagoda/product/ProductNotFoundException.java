package com.zlagoda.product;

import com.zlagoda.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException() {
        super("Not found such product");
    }
}
