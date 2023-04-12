package com.zlagoda.card;

import com.zlagoda.helpers.Service;

import java.util.List;

public interface CustomerCardService extends Service<String, CustomerCardDto> {
    List<String> getCities();

    List<String> getStreets();

    List<String> getZipCodes();
}
