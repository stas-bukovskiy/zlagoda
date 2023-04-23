package com.zlagoda.category;

import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.product.ProductDeleteConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    public final static Sort DEFAULT_SORT = Sort.by("category_name");

    private final CategoryRepository repository;
    private final CategoryConverter converter;
    private final ProductDeleteConfirmationService productDeleteConfirmationService;

    @Override
    public List<CategoryDto> getAll(Sort sort) {
        return repository.findAll(sort)
                .stream()
                .map(converter::convertToDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return repository.findById(id)
                .map(converter::convertToDto)
                .orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        Category categoryToCreate = converter.convertToEntity(categoryDto);
        Category createdCategory = repository.save(categoryToCreate);
        return converter.convertToDto(createdCategory);
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        if (!repository.existsById(id))
            throw new CategoryNotFoundException();
        Category categoryToUpdate = converter.convertToEntity(categoryDto);
        Category updatedCategory = repository.update(id, categoryToUpdate);
        return converter.convertToDto(updatedCategory);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public CategoryDto deleteById(Long id) {
        CategoryDto categoryToDelete = getById(id);
        productDeleteConfirmationService.confirmDeletion(id);
        repository.deleteById(id);
        return categoryToDelete;
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
        confirmation.setObjectName("Category");
        confirmation.setChildRemovals(productDeleteConfirmationService.createChildDeleteConfirmation(id));
        return confirmation;
    }
}
