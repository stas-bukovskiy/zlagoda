package com.zlagoda.helpers;

import java.util.List;
import java.util.Optional;

public interface Repository<E extends Entity, ID> {
    List<E> findAll();

    Optional<E> findById(ID id);

    E save(E e);

    E update(ID id, E e);

    Optional<E> deleteById(ID id);

    void deleteAll();
}
