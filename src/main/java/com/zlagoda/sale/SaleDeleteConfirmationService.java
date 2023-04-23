package com.zlagoda.sale;

import com.zlagoda.check.CheckDeleteByIdConfirmationService;
import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.helpers.DeleteConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleDeleteConfirmationService implements DeleteConfirmationService<String> {

    private final SaleRepository repository;
    private final CheckDeleteByIdConfirmationService checkDeleteConfirmationService;

    @Override
    public List<DeleteConfirmation> createChildDeleteConfirmation(String upc) {
        List<DeleteConfirmation> deleteConfirmations = new ArrayList<>();
        repository.findAllByUpc(upc).forEach(sale -> {
            deleteConfirmations.add(new DeleteConfirmation(sale.getStoreProduct().getUpc(), "Sale", null));
            deleteConfirmations.addAll(checkDeleteConfirmationService.createChildDeleteConfirmation(sale.getCheckNumber()));
        });
        return deleteConfirmations;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void confirmDeletion(String upc) {
        repository.findAllByUpc(upc).forEach(sale -> {
            checkDeleteConfirmationService.confirmDeletion(sale.getCheckNumber());
            repository.deleteAllByUpc(upc);
        });
    }
}
