package com.zlagoda.card;

import com.zlagoda.check.CheckDeleteByCardIdConfirmationService;
import com.zlagoda.confiramtion.DeleteConfirmation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerCardServiceImpl implements CustomerCardService {

    private final CustomerCardRepository repository;
    private final CustomerCardConverter converter;
    private final CheckDeleteByCardIdConfirmationService checkDeleteConfirmationService;

    @Override
    public List<CustomerCardDto> getAll() {
        return repository.findAll()
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
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public CustomerCardDto deleteById(String id) {
        CustomerCardDto cardToDelete = getById(id);
        checkDeleteConfirmationService.confirmDeletion(id);
        repository.deleteById(id);
        return cardToDelete;
    }

    @Override
    public List<CustomerCardDto> getAllByPercent(int percent) {
        return repository.findALlByPercent(percent).stream()
                .map(converter::convertToDto)
                .toList();
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

    @Override
    public DeleteConfirmation createDeleteConfirmation(String id) {
        DeleteConfirmation confirmation = new DeleteConfirmation();
        confirmation.setId(id);
        confirmation.setObjectName("Customer card");
        confirmation.setChildRemovals(checkDeleteConfirmationService.createChildDeleteConfirmation(id));
        return confirmation;
    }

    @Override
    public List<CustomerCardDto> getAllBySurname(String surname) {
        return repository.findAllBySurname(surname).stream()
                .map(converter::convertToDto)
                .toList();
    }
}
