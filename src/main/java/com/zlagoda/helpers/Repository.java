package com.zlagoda.helpers;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface Repository<E extends Entity, ID> {
    List<E> findAll(Sort sort);

    Optional<E> findById(ID id);

    E save(E e);

    E update(ID id, E e);

    Optional<E> deleteById(ID id);

    void deleteAll();
}
