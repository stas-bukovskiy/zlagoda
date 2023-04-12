package com.zlagoda.card;

import com.zlagoda.helpers.Repository;

import java.util.List;

public interface CustomerCardRepository extends Repository<CustomerCard, String> {

    List<String> findAllDistinctCities();

    List<String> findAllDistinctStreets();

    List<String> findAllDistinctZipCodes();

    boolean existsById(String id);
}
