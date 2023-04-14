package com.zlagoda.product;

import com.zlagoda.helpers.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.zlagoda.utils.DateConverter.convertToDate;
import static com.zlagoda.utils.DateConverter.convertToDateString;

@Component
public class ProductConverter extends Converter<Product, ProductDto> {

    public ProductConverter() {
        super(new ModelMapper(), Product.class, ProductDto.class);
        mapper.createTypeMap(Product.class, ProductDto.class)
                .addMappings(mapping -> mapping.using(ctx -> ctx.getSource() == null ? null : convertToDateString((Date) ctx.getSource()))
                        .map(Product::getExpirationDate, ProductDto::setExpirationDate));
        mapper.createTypeMap(ProductDto.class, Product.class)
                .addMappings(mapping -> mapping.using(ctx -> ctx.getSource() == null ? null : convertToDate((String) ctx.getSource()))
                        .map(ProductDto::getExpirationDate, Product::setExpirationDate));
    }

}
