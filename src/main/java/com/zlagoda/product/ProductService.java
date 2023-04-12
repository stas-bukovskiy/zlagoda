package com.zlagoda.product;

import com.zlagoda.helpers.Service;

public interface ProductService extends Service<Long, ProductDto> {

    boolean isNameUniqueToCreate(String name);

    boolean isNameUniqueToUpdate(Long id, String name);
}
