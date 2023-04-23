package com.zlagoda.sale;

import com.zlagoda.helpers.Repository;
import org.springframework.data.util.Pair;

import java.util.List;

public interface SaleRepository extends Repository<Sale, Pair<String, String>> {
    List<Sale> findAllByCheckNumber(String checkNumber);

    boolean existsById(Pair<String, String> upcAndCheckNumber);

    List<Sale> findAllByUpc(String upc);

    void deleteAllByUpc(String upc);
}
