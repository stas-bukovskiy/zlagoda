package com.zlagoda.category;

import com.zlagoda.helpers.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter extends Converter<Category, CategoryDto> {
    public CategoryConverter() {
        super(new ModelMapper(), Category.class, CategoryDto.class);
    }
}
