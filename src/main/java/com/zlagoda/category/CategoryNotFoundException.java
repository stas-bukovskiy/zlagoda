package com.zlagoda.category;

public class CategoryNotFoundException extends NumberFormatException {
    public CategoryNotFoundException() {
        super("Not found such category");
    }
}
