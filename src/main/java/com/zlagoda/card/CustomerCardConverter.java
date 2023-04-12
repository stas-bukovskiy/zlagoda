package com.zlagoda.card;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerCardConverter extends com.zlagoda.helpers.Converter<CustomerCard, CustomerCardDto> {

    public CustomerCardConverter() {
        super(new ModelMapper(), CustomerCard.class, CustomerCardDto.class);
    }

}
