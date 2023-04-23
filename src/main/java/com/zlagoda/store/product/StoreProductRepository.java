package com.zlagoda.store.product;

import com.zlagoda.helpers.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StoreProductRepository extends Repository<StoreProduct, String> {
    boolean existsById(String upc);

    Optional<BigDecimal> findPriceById(String upc);

    Optional<Integer> findProductsNumberById(String upc);

    void subtractAmountByUpc(String upc, int delta);

    List<StoreProduct> findAllByProductId(Long productId);
}

