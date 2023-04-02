package com.zlagoda.helpers;

import org.springframework.data.domain.Sort;

import java.util.List;

public interface Service<E extends Entity, ID, D extends DTO> {
    List<E> getAll(Sort sort);

    E getById(ID id);

    E create(D d);

    E update(ID id, D D);

    E deleteById(ID id);
}
