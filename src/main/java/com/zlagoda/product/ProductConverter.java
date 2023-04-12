package com.zlagoda.product;

import com.zlagoda.helpers.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter extends Converter<Product, ProductDto> {

    public ProductConverter() {
        super(new ModelMapper(), Product.class, ProductDto.class);
    }

}
