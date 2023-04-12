package com.zlagoda.product;

import com.zlagoda.helpers.Repository;

public interface ProductRepository extends Repository<Product, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String username, Long id);

    boolean existsById(Long id);
}
