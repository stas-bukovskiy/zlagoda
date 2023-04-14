package com.zlagoda.store.product;

import com.zlagoda.helpers.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class StoreProductConverter extends Converter<StoreProduct, StoreProductDto> {

    public StoreProductConverter() {
        super(new ModelMapper(), StoreProduct.class, StoreProductDto.class);
    }

}
