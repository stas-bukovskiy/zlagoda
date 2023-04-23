package com.zlagoda.category;

import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.helpers.Service;

public interface CategoryService extends Service<Long, CategoryDto> {

    boolean isNameUniqueToCreate(String name);

    boolean isNameUniqueToUpdate(Long id, String name);

    DeleteConfirmation createDeleteConfirmation(Long id);
}
