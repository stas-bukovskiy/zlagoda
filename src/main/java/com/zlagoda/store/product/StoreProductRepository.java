package com.zlagoda.store.product;

import com.zlagoda.helpers.Repository;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StoreProductRepository extends Repository<StoreProduct, String> {

    List<StoreProduct> findAllPromotional(Sort sort);

    List<StoreProduct> findAllNotPromotional(Sort sort);

    boolean existsById(String upc);

    Optional<BigDecimal> findPriceById(String upc);

    Optional<Integer> findProductsNumberById(String upc);

    void subtractAmountByUpc(String upc, int delta);

    List<StoreProduct> findAllByProductId(Long productId);

    boolean existsByProductId(Long id);

    boolean existsByProductIdAndUpcIsNot(Long productId, String upc);
}

