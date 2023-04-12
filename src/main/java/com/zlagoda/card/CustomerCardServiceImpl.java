package com.zlagoda.card;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerCardServiceImpl implements CustomerCardService {

    public final static Sort DEFAULT_SORT = Sort.by("cust_name");

    private final CustomerCardRepository repository;
    private final CustomerCardConverter converter;

    @Override
    public List<CustomerCardDto> getAll(Sort sort) {
        return repository.findAll(sort)
                .stream()
                .map(converter::convertToDto)
                .toList();
    }

    @Override
    public CustomerCardDto getById(String id) {
        return repository.findById(id)
                .map(converter::convertToDto)
                .orElseThrow(CustomerCardNotFoundException::new);
    }

    @Override
    public CustomerCardDto create(CustomerCardDto cardDto) {
        CustomerCard cardToCreate = converter.convertToEntity(cardDto);
        CustomerCard createdCard = repository.save(cardToCreate);
        return converter.convertToDto(createdCard);
    }

    @Override
    public CustomerCardDto update(String id, CustomerCardDto cardDto) {
        if (!repository.existsById(id))
            throw new CustomerCardNotFoundException();
        CustomerCard cardToUpdate = converter.convertToEntity(cardDto);
        CustomerCard updatedCard = repository.update(id, cardToUpdate);
        return converter.convertToDto(updatedCard);
    }

    @Override
    public CustomerCardDto deleteById(String id) {
        return repository.deleteById(id)
                .map(converter::convertToDto)
                .orElseThrow(CustomerCardNotFoundException::new);
    }

    @Override
    public List<String> getCities() {
        return repository.findAllDistinctCities();
    }

    @Override
    public List<String> getStreets() {
        return repository.findAllDistinctStreets();
    }

    @Override
    public List<String> getZipCodes() {
        return repository.findAllDistinctZipCodes();
    }
}
