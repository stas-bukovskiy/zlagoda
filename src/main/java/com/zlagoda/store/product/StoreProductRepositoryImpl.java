package com.zlagoda.store.product;

import com.zlagoda.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.zlagoda.utils.SortUtils.sortToString;

@Repository
@RequiredArgsConstructor
public class StoreProductRepositoryImpl implements StoreProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StoreProductRowMapper rowMapper;

    @Override
    public List<StoreProduct> findAll() {
        String sql = """
                SELECT sp.upc                      as "upc",
                       sp.upc_prom                 as "upc_prom",
                       sp.id_product               as "id_product",
                       sp.selling_price            as "selling_price",
                       sp.products_number          as "products_number",
                       sp.promotional_product      as "promotional_product",
                       p.category_number           as "category_number",
                       p.product_name              as "product_name",
                       p.characteristics           as "characteristics",
                       prom_sp.upc                 as "prom_upc",
                       prom_sp.upc_prom            as "prom_upc_prom",
                       prom_sp.id_product          as "prom_id_product",
                       prom_sp.selling_price       as "prom_selling_price",
                       prom_sp.products_number     as "prom_products_number",
                       prom_sp.promotional_product as "prom_promotional_product"
                FROM store_product sp
                         JOIN product p on p.id_product = sp.id_product
                         LEFT JOIN store_product prom_sp on prom_sp.upc = sp.upc_prom
                ORDER BY sp.products_number;
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<StoreProduct> findById(String upc) {
        String sql = """
                SELECT sp.upc                      as "upc",
                       sp.upc_prom                 as "upc_prom",
                       sp.id_product               as "id_product",
                       sp.selling_price            as "selling_price",
                       sp.products_number          as "products_number",
                       sp.promotional_product      as "promotional_product",
                       p.category_number           as "category_number",
                       p.product_name              as "product_name",
                       p.characteristics           as "characteristics",
                       prom_sp.upc                 as "prom_upc",
                       prom_sp.upc_prom            as "prom_upc_prom",
                       prom_sp.id_product          as "prom_id_product",
                       prom_sp.selling_price       as "prom_selling_price",
                       prom_sp.products_number     as "prom_products_number",
                       prom_sp.promotional_product as "prom_promotional_product"
                FROM store_product sp
                         JOIN product p on p.id_product = sp.id_product
                         LEFT JOIN store_product prom_sp on prom_sp.upc = sp.upc_prom
                WHERE sp.upc = ?;
                """;
        List<StoreProduct> store_products = jdbcTemplate.query(sql, rowMapper, upc);
        return store_products.isEmpty() ? Optional.empty() : Optional.of(store_products.get(0));
    }

    @Override
    public StoreProduct save(StoreProduct storeProduct) {
        String sql = """
                INSERT INTO store_product (upc,
                                           upc_prom,
                                           id_product,
                                           selling_price,
                                           products_number,
                                           promotional_product)
                VALUES (?, ?, ?, ?, ?, ?);
                """;
        jdbcTemplate.update(sql,
                storeProduct.getUpc(),
                storeProduct.getPromStoreProduct() == null ? null : storeProduct.getPromStoreProduct().getUpc(),
                storeProduct.getProduct().getId(),
                storeProduct.getSellingPrice(),
                storeProduct.getProductsNumber(),
                storeProduct.isPromotional());
        return storeProduct;
    }

    @Override
    public StoreProduct update(String upc, StoreProduct storeProduct) {
        String sql = """
                UPDATE store_product
                SET upc_prom=?, id_product=?, selling_price=?, products_number=?, promotional_product=?
                WHERE upc=?;
                """;
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
            String sql = """
                    DELETE
                    FROM store_product
                    WHERE upc = ?;
                    """;
            jdbcTemplate.update(sql, upc);
        }
        return store_productOptional;
    }

    @Override
    public void deleteAll() {
        String sql = """
                DELETE
                FROM store_product;
                """;
        jdbcTemplate.update(sql);
    }

    @Override
    public List<StoreProduct> findAllPromotional(Sort sort) {
        String sql = """
                SELECT sp.upc                      as "upc",
                       sp.upc_prom                 as "upc_prom",
                       sp.id_product               as "id_product",
                       sp.selling_price            as "selling_price",
                       sp.products_number          as "products_number",
                       sp.promotional_product      as "promotional_product",
                       p.category_number           as "category_number",
                       p.product_name              as "product_name",
                       p.characteristics           as "characteristics"
                FROM store_product sp
                         JOIN product p on p.id_product = sp.id_product
                WHERE sp.promotional_product
                ORDER BY ?
                """;

        return jdbcTemplate.query(sql, createResultSetExtractor(), sortToString(sort));
    }

    @Override
    public List<StoreProduct> findAllNotPromotional(Sort sort) {
        String sql = """
                SELECT sp.upc                      as "upc",
                       sp.upc_prom                 as "upc_prom",
                       sp.id_product               as "id_product",
                       sp.selling_price            as "selling_price",
                       sp.products_number          as "products_number",
                       sp.promotional_product      as "promotional_product",
                       p.category_number           as "category_number",
                       p.product_name              as "product_name",
                       p.characteristics           as "characteristics"
                FROM store_product sp
                JOIN product p on p.id_product = sp.id_product
                WHERE sp.promotional_product = false
                ORDER BY ?;
                """;

        return jdbcTemplate.query(sql, createResultSetExtractor(), sortToString(sort));
    }

    private ResultSetExtractor<List<StoreProduct>> createResultSetExtractor() {
        return rs -> {
            List<StoreProduct> storeProducts = new LinkedList<>();
            while (rs.next()) {
                storeProducts.add(new StoreProduct(
                        rs.getString("upc"),
                        rs.getBigDecimal("selling_price"),
                        rs.getInt("products_number"),
                        rs.getBoolean("promotional_product"),
                        new Product(
                                rs.getLong("id_product"),
                                rs.getString("product_name"),
                                rs.getString("characteristics"),
                                rs.getLong("category_number")
                        ),
                        null));
            }
            return storeProducts;
        };
    }

    @Override
    public boolean existsById(String upc) {
        String sql = """
                SELECT COUNT(*)
                FROM store_product
                WHERE upc = ?;
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, upc);
        if (count == null)
            return false;
        return count > 0;
    }

    @Override
    public Optional<BigDecimal> findPriceById(String upc) {
        String sql = """
                SELECT selling_price
                FROM store_product
                WHERE upc = ?;
                """;
        BigDecimal price = jdbcTemplate.queryForObject(sql, BigDecimal.class, upc);
        return price == null ? Optional.empty() : Optional.of(price);

    }

    @Override
    public Optional<Integer> findProductsNumberById(String upc) {
        String sql = """
                SELECT products_number
                FROM store_product
                WHERE upc = ?;
                """;
        Integer productsNumber = jdbcTemplate.queryForObject(sql, Integer.class, upc);
        return productsNumber == null ? Optional.empty() : Optional.of(productsNumber);
    }

    @Override
    public void subtractAmountByUpc(String upc, int delta) {
        String sql = """
                UPDATE store_product
                SET products_number = products_number - ?
                WHERE upc = ?
                """;
        jdbcTemplate.update(sql, delta, upc);
    }

    @Override
    public List<StoreProduct> findAllByProductId(Long productId) {
        String sql = """
                SELECT sp.upc                      as "upc",
                       sp.upc_prom                 as "upc_prom",
                       sp.id_product               as "id_product",
                       sp.selling_price            as "selling_price",
                       sp.products_number          as "products_number",
                       sp.promotional_product      as "promotional_product",
                       p.category_number           as "category_number",
                       p.product_name              as "product_name",
                       p.characteristics           as "characteristics",
                       prom_sp.upc                 as "prom_upc",
                       prom_sp.upc_prom            as "prom_upc_prom",
                       prom_sp.id_product          as "prom_id_product",
                       prom_sp.selling_price       as "prom_selling_price",
                       prom_sp.products_number     as "prom_products_number",
                       prom_sp.promotional_product as "prom_promotional_product"
                FROM store_product sp
                         JOIN product p on p.id_product = sp.id_product
                         LEFT JOIN store_product prom_sp on prom_sp.upc = sp.upc_prom
                WHERE sp.id_product = ?
                """;

        return jdbcTemplate.query(sql, rowMapper, productId);
    }

    @Override
    public boolean existsByProductId(Long productId) {
        String sql = """
                SELECT COUNT(*)
                FROM store_product
                WHERE id_product = ?;
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId);
        if (count == null)
            return false;
        return count > 0;
    }

    @Override
    public boolean existsByProductIdAndUpcIsNot(Long productId, String upc) {
        String sql = """
                SELECT COUNT(*)
                FROM store_product
                WHERE id_product = ? AND upc != ? AND promotional_product = false;
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId, upc);
        if (count == null)
            return false;
        return count > 0;
    }

    @Override
    public boolean isPromotionalByUpc(String upc) {
        String sql = """
                SELECT promotional_product
                FROM store_product
                WHERE upc = ?
                """;
        Boolean isPromotional = jdbcTemplate.queryForObject(sql, Boolean.class, upc);
        if (isPromotional == null)
            return false;
        return isPromotional;
    }

}
