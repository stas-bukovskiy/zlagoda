package com.zlagoda.helpers;

import com.zlagoda.confiramtion.DeleteConfirmation;

import java.util.List;

public interface DeleteConfirmationService<ID> {

    List<DeleteConfirmation> createChildDeleteConfirmation(ID id);

    void confirmDeletion(ID id);

}
