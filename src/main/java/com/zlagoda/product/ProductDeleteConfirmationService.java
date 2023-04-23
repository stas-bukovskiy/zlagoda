package com.zlagoda.product;

import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.helpers.DeleteConfirmationService;
import com.zlagoda.store.product.StoreProductDeleteConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDeleteConfirmationService implements DeleteConfirmationService<Long> {

    private final ProductRepository productRepository;
    private final StoreProductDeleteConfirmationService storeProductDeleteConfirmationService;

    @Override
    public List<DeleteConfirmation> createChildDeleteConfirmation(Long categoryId) {
        List<DeleteConfirmation> deleteConfirmations = new ArrayList<>();
        productRepository.findAllByCategoryId(categoryId).forEach(product -> {
            deleteConfirmations.add(new DeleteConfirmation(product.getId().toString(), "Product", null));
            deleteConfirmations.addAll(storeProductDeleteConfirmationService.createChildDeleteConfirmation(product.getId()));
        });
        return deleteConfirmations;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void confirmDeletion(Long categoryId) {
        productRepository.findAllByCategoryId(categoryId).forEach(product -> {
            storeProductDeleteConfirmationService.confirmDeletion(product.getId());
            productRepository.deleteById(product.getId());
        });
    }
}
