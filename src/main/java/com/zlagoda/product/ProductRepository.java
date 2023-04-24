package com.zlagoda.product;

import com.zlagoda.helpers.Repository;

import java.util.List;

public interface ProductRepository extends Repository<Product, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String username, Long id);

    boolean existsById(Long id);

    List<Product> findAllByCategoryId(Long categoryId);

    List<Product> findAllByName(String name);
}
