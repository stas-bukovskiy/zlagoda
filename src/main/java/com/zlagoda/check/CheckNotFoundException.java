package com.zlagoda.check;

import com.zlagoda.exception.NotFoundException;

public class CheckNotFoundException extends NotFoundException {
    public CheckNotFoundException() {
        super("Not found such check");
    }
}
