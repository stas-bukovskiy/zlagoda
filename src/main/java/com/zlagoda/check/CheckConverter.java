package com.zlagoda.check;

import com.zlagoda.card.CustomerCard;
import com.zlagoda.helpers.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CheckConverter extends Converter<Check, CheckDto> {

    public CheckConverter() {
        super(new ModelMapper(), Check.class, CheckDto.class);
        mapper.getConfiguration()
                .setAmbiguityIgnored(true);

        mapper.createTypeMap(Check.class, CheckDto.class)
                .addMappings(mapping -> {
                    mapping.map(source -> source.getCustomerCard().getCardNumber(), CheckDto::setCardNumber);
                });
        mapper.createTypeMap(CheckDto.class, Check.class)
                .addMappings(mapping -> {
                    mapping.skip(Check::setSales);
                    mapping.<String>map(CheckDto::getCheckNumber,
                            ((destination, value) -> destination.setCustomerCard(CustomerCard.builder()
                                    .cardNumber(value).build())));
                });
    }

}
