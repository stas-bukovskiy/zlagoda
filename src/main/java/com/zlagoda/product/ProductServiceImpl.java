package com.zlagoda.product;

import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.store.product.StoreProductDeleteConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductConverter converter;
    private final StoreProductDeleteConfirmationService storeProductDeleteConfirmationService;

    @Override
    public List<ProductDto> getAll() {
        return repository.findAll()
                .stream()
                .map(converter::convertToDto)
                .toList();
    }

    @Override
    public ProductDto getById(Long id) {
        return repository.findById(id)
                .map(converter::convertToDto)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public ProductDto create(ProductDto productDto) {
        Product productToCreate = converter.convertToEntity(productDto);
        Product createdProduct = repository.save(productToCreate);
        return converter.convertToDto(createdProduct);
    }

    @Override
    public ProductDto update(Long id, ProductDto productDto) {
        if (!repository.existsById(id))
            throw new ProductNotFoundException();
        Product productToUpdate = converter.convertToEntity(productDto);
        Product updatedProduct = repository.update(id, productToUpdate);
        return converter.convertToDto(updatedProduct);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public ProductDto deleteById(Long id) {
        ProductDto productToDelete = getById(id);
        storeProductDeleteConfirmationService.confirmDeletion(id);
        repository.deleteById(id);
        return productToDelete;
    }

    @Override
    public List<ProductDto> getALlByCategoryId(Long categoryId) {
        return repository.findAllByCategoryId(categoryId).stream()
                .map(converter::convertToDto)
                .toList();
    }

    @Override
    public boolean isNameUniqueToCreate(String name) {
        return !repository.existsByName(name);
    }

    @Override
    public boolean isNameUniqueToUpdate(Long id, String name) {
        return !repository.existsByNameAndIdIsNot(name, id);
    }

    @Override
    public DeleteConfirmation createDeleteConfirmation(Long id) {
        DeleteConfirmation confirmation = new DeleteConfirmation();
        confirmation.setId(id.toString());
        confirmation.setObjectName("Product");
        confirmation.setChildRemovals(storeProductDeleteConfirmationService.createChildDeleteConfirmation(id));
        return confirmation;
    }

    @Override
    public List<ProductDto> getAllByName(String name) {
        return repository.findAllByName(name).stream()
                .map(converter::convertToDto)
                .toList();
    }
}
