package com.zlagoda.store.product;

import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.helpers.DeleteConfirmationService;
import com.zlagoda.sale.SaleDeleteConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreProductDeleteConfirmationService implements DeleteConfirmationService<Long> {

    private final StoreProductRepository repository;
    private final SaleDeleteConfirmationService saleDeleteConfirmationService;

    @Override
    public List<DeleteConfirmation> createChildDeleteConfirmation(Long productId) {
        List<DeleteConfirmation> deleteConfirmations = new ArrayList<>();
        repository.findAllByProductId(productId).forEach(storeProduct -> {
            deleteConfirmations.add(new DeleteConfirmation(storeProduct.getUpc(), "Store product", null));
            deleteConfirmations.addAll(saleDeleteConfirmationService.createChildDeleteConfirmation(storeProduct.getUpc()));
        });
        return deleteConfirmations;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void confirmDeletion(Long productId) {
        repository.findAllByProductId(productId).forEach(storeProduct -> {
            saleDeleteConfirmationService.confirmDeletion(storeProduct.getUpc());
            repository.deleteById(storeProduct.getUpc());
        });
    }
}
