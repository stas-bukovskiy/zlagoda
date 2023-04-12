package com.zlagoda.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    public final static Sort DEFAULT_SORT = Sort.by("category_name");

    private final CategoryRepository repository;
    private final CategoryConverter converter;

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
    public CategoryDto deleteById(Long id) {
        return repository.deleteById(id)
                .map(converter::convertToDto)
                .orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public boolean isNameUniqueToCreate(String name) {
        return !repository.existsByName(name);
    }

    @Override
    public boolean isNameUniqueToUpdate(Long id, String name) {
        return !repository.existsByNameAndIdIsNot(name, id);
    }
}
