package com.zlagoda.product;

import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.helpers.Service;

import java.util.List;

public interface ProductService extends Service<Long, ProductDto> {

    List<ProductDto> getALlByCategoryId(Long categoryId);

    boolean isNameUniqueToCreate(String name);

    boolean isNameUniqueToUpdate(Long id, String name);

    DeleteConfirmation createDeleteConfirmation(Long id);

    List<ProductDto> getAllByName(String name);
}
