package com.zlagoda.store.product;

import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.helpers.Service;
import org.springframework.data.domain.Sort;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.util.List;

public interface StoreProductService extends Service<String, StoreProductDto> {

    List<StoreProductDto> getAllPromotional(Sort sort);

    List<StoreProductDto> getAllNotPromotional(Sort sort);


    boolean isUniqueToCreate(String upc);

    boolean isUniqueToUpdate(String oldUpc, String newUpc);

    BigDecimal getPriceByUpc(String upc);

    int getAmountByUpc(String upc);

    void makeStoreProductPromotional(String upc);

    void unmakeStoreProductPromotional(String upc);

    void subtractAmountByUpc(String storeProductUpc, int productNumber);

    DeleteConfirmation createDeleteConfirmation(String upc);

    List<FieldError> checkProductIdToCreate(StoreProductDto storeProductDto);

    List<FieldError> checkProductIdToUpdate(StoreProductDto storeProductDto);
}
