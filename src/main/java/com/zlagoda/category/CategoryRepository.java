package com.zlagoda.category;

import com.zlagoda.helpers.Repository;

public interface CategoryRepository extends Repository<Category, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, Long id);

    boolean existsById(Long id);
}
