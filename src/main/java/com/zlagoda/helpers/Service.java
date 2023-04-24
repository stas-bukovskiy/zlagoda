package com.zlagoda.helpers;

import java.util.List;

public interface Service<ID, D extends DTO> {
    List<D> getAll();

    D getById(ID id);

    D create(D d);

    D update(ID id, D D);

    D deleteById(ID id);
}
