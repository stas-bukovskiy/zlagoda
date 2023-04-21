package com.zlagoda.sale;

import com.zlagoda.exception.NotFoundException;

public class SaleNotFoundException extends NotFoundException {
    public SaleNotFoundException() {
        super("Not found such sale");
    }
}
