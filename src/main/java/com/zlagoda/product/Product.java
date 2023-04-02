package com.zlagoda.product;

import com.zlagoda.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Product {

    private Long id;
    private String name;
    private String characteristics;
    private Category category;

}
