package com.zlagoda.check;

import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.helpers.DeleteConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckDeleteByIdConfirmationService implements DeleteConfirmationService<String> {

    private final CheckRepository repository;

    @Override
    public List<DeleteConfirmation> createChildDeleteConfirmation(String checkId) {
        return repository.findById(checkId).stream()
                .map(check -> new DeleteConfirmation(check.getCheckNumber(), "Check", null))
                .toList();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void confirmDeletion(String checkId) {
        repository.deleteById(checkId);
    }
}


