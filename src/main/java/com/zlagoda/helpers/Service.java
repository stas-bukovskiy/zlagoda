package com.zlagoda.helpers;

import org.springframework.data.domain.Sort;

import java.util.List;

public interface Service<ID, D extends DTO> {
    List<D> getAll(Sort sort);

    D getById(ID id);

    D create(D d);

    D update(ID id, D D);

    D deleteById(ID id);
}
