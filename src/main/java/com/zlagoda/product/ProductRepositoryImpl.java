package com.zlagoda.product;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ProductRowMapper rowMapper;

    @Override
    public List<Product> findAll() {
        String sql = """
                SELECT *
                FROM product
                ORDER BY product_name;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Product> findById(Long id) {
        String sql = """
                SELECT *
                FROM product
                WHERE id_product = ?
                """;
        List<Product> products = jdbcTemplate.query(sql, rowMapper, id);
        return products.isEmpty() ? Optional.empty() : Optional.of(products.get(0));
    }

    @Override
    public Product save(Product product) {
        String sql = """
                INSERT INTO product (category_number, product_name, characteristics)
                VALUES (?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id_product"});
                    ps.setLong(1, product.getCategoryId());
                    ps.setString(2, product.getName());
                    ps.setString(3, product.getCharacteristics());
                    return ps;
                },
                keyHolder);
        Long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        product.setId(generatedId);
        return product;
    }

    @Override
    public Product update(Long id, Product product) {
        String sql = """
                UPDATE product
                SET category_number=?, product_name=?, characteristics=?
                WHERE id_product=?;
                """;
        jdbcTemplate.update(sql,
                product.getCategoryId(),
                product.getName(),
                product.getCharacteristics(),
                product.getId());
        return product;
    }

    @Override
    public Optional<Product> deleteById(Long id) {
        Optional<Product> productOptional = findById(id);
        if (productOptional.isPresent()) {
            String sql = """
                    DELETE
                    FROM product
                    WHERE id_product = ?;
                    """;
            jdbcTemplate.update(sql, id);
        }
        return productOptional;
    }

    @Override
    public void deleteAll() {
        String sql = """
                DELETE
                FROM product;
                """;
        jdbcTemplate.update(sql);
    }

    @Override
    public boolean existsByName(String name) {
        String sql = """
                SELECT COUNT(*)
                FROM product
                WHERE product_name = ?;
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        if (count == null)
            return false;
        return count > 0;
    }

    @Override
    public boolean existsById(Long id) {
        String sql = """
                SELECT COUNT(*)
                FROM product
                WHERE id_product = ?;
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (count == null)
            return false;
        return count > 0;
    }

    @Override
    public List<Product> findAllByCategoryId(Long categoryId) {
        String sql = """
                SELECT *
                FROM product
                WHERE category_number = ?
                ORDER BY product_name;
                """;
        return jdbcTemplate.query(sql, rowMapper, categoryId);
    }

    @Override
    public List<Product> findAllByName(String name) {
        String sql = """
                SELECT *
                FROM product
                WHERE LOWER(product_name) LIKE '%' || LOWER(?) || '%';
                """;
        return jdbcTemplate.query(sql, rowMapper, name);
    }

    @Override
    public boolean existsByNameAndIdIsNot(String name, Long id) {
        String sql = """
                SELECT COUNT(*)
                FROM product
                WHERE product_name = ? AND id_product != ?;
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name, id);
        if (count == null)
            return false;
        return count > 0;
    }
}
