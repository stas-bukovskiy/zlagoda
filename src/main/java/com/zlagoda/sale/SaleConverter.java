package com.zlagoda.sale;

import com.zlagoda.helpers.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SaleConverter extends Converter<Sale, SaleDto> {

    public SaleConverter() {
        super(new ModelMapper(), Sale.class, SaleDto.class);
    }

}
