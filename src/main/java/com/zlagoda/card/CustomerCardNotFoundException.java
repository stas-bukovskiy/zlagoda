package com.zlagoda.card;

import com.zlagoda.exception.NotFoundException;

public class CustomerCardNotFoundException extends NotFoundException {
    public CustomerCardNotFoundException() {
        super("Not found such customer card");
    }
}
