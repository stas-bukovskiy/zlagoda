package com.zlagoda.card;

import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.helpers.Service;

import java.util.List;

public interface CustomerCardService extends Service<String, CustomerCardDto> {

    List<CustomerCardDto> getAllByPercent(int percent);

    List<String> getCities();

    List<String> getStreets();

    List<String> getZipCodes();

    DeleteConfirmation createDeleteConfirmation(String id);

    List<CustomerCardDto> getAllBySurname(String surname);
}
