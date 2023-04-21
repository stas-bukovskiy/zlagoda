package com.zlagoda.utils;

import com.zlagoda.product.ProductDto;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class ProductFormatter implements Formatter<ProductDto> {

    @Override
    public ProductDto parse(String text, Locale locale) throws ParseException {
        final Long productId = Long.valueOf(text);
        return ProductDto.builder().id(productId).build();
    }

    @Override
    public String print(ProductDto object, Locale locale) {
        return (object != null ? object.getId().toString() : "");
    }
}