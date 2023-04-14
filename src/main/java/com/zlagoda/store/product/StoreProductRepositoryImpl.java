package com.zlagoda.store.product;

import com.zlagoda.exception.EntityLinkException;
import com.zlagoda.exception.NotFoundException;
import com.zlagoda.product.Product;
import com.zlagoda.product.ProductNotFoundException;
import com.zlagoda.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.zlagoda.utils.SortUtils.sortToString;

@Repository
@RequiredArgsConstructor
public class StoreProductRepositoryImpl implements StoreProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StoreProductRowMapper rowMapper;
    private final ProductRepository productRepository;

    @Override
    public List<StoreProduct> findAll(Sort sort) {
        String sql = "SELECT * " +
                "FROM store_product " +
                "ORDER BY " + sortToString(sort);
        return jdbcTemplate.query(sql, rowMapper).stream()
                .map(this::linkNestedEntities)
                .toList();
    }

    @Override
    public Optional<StoreProduct> findById(String upc) {
        String sql = "SELECT * " +
                "FROM store_product " +
                "WHERE upc = ?";
        List<StoreProduct> store_products = jdbcTemplate.query(sql, rowMapper, upc);
        return store_products.isEmpty() ? Optional.empty() : Optional.of(linkNestedEntities(store_products.get(0)));
    }

    @Override
    public StoreProduct save(StoreProduct storeProduct) {
        String sql = "INSERT INTO store_product (upc, upc_prom, id_product, selling_price, products_number, promotional_product) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                storeProduct.getUpc(),
                storeProduct.getPromStoreProduct() == null ? null : storeProduct.getPromStoreProduct().getUpc(),
                storeProduct.getProduct().getId(),
                storeProduct.getSellingPrice(),
                storeProduct.getSellingPrice(),
                storeProduct.isPromotional());
        return storeProduct;
    }

    @Override
    public StoreProduct update(String upc, StoreProduct storeProduct) {
        String sql = "UPDATE store_product " +
                "SET upc_prom=?, id_product=?, selling_price=?, products_number=?, promotional_product=? " +
                "WHERE upc=?";
        jdbcTemplate.update(sql,
                storeProduct.getPromStoreProduct() == null ? null : storeProduct.getPromStoreProduct().getUpc(),
                storeProduct.getProduct().getId(),
                storeProduct.getSellingPrice(),
                storeProduct.getProductsNumber(),
                storeProduct.isPromotional(),
                storeProduct.getUpc());
        return storeProduct;
    }

    @Override
    public Optional<StoreProduct> deleteById(String upc) {
        Optional<StoreProduct> store_productOptional = findById(upc);
        if (store_productOptional.isPresent()) {
            String sql = "DELETE FROM store_product WHERE upc = ?";
            jdbcTemplate.update(sql, upc);
        }
        return store_productOptional;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM store_product";
        jdbcTemplate.update(sql);
    }

    @Override
    public boolean existsById(String upc) {
        String sql = "SELECT COUNT(*) FROM store_product WHERE upc = ?";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, upc);
            return count > 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private StoreProduct linkNestedEntities(StoreProduct storeProduct) {
        try {
            linkProduct(storeProduct);
            if (storeProduct.getPromStoreProduct() != null)
                linkPromStoreProduct(storeProduct);
        } catch (NotFoundException e) {
            throw new EntityLinkException("cannot link nested entity to store product", e);
        }
        return storeProduct;
    }

    private void linkPromStoreProduct(StoreProduct storeProduct) {
        String promStoreProductUpc = storeProduct.getPromStoreProduct().getUpc();
        StoreProduct promStoreProduct = findById(promStoreProductUpc)
                .orElseThrow(StoreProductNotFoundException::new);
        promStoreProduct.setPromStoreProduct(promStoreProduct);
    }

    private void linkProduct(StoreProduct storeProduct) {
        Long productId = storeProduct.getProduct().getId();
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        storeProduct.setProduct(product);
    }
}
