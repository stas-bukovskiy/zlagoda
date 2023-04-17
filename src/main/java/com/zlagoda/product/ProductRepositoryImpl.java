package com.zlagoda.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.zlagoda.utils.SortUtils.sortToString;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ProductRowMapper rowMapper;

    @Override
    public List<Product> findAll(Sort sort) {
        String sql = "SELECT * " +
                "FROM product " +
                "ORDER BY " + sortToString(sort);
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "SELECT * " +
                "FROM product " +
                "WHERE id_product = ?";
        List<Product> products = jdbcTemplate.query(sql, rowMapper, id);
        return products.isEmpty() ? Optional.empty() : Optional.of(products.get(0));
    }

    @Override
    public Product save(Product product) {
        String sql = "INSERT INTO product (category_number, product_name, characteristics, expiration_date) " +
                "VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id_product"});
                    ps.setLong(1, product.getCategoryId());
                    ps.setString(2, product.getName());
                    ps.setString(3, product.getCharacteristics());
                    ps.setDate(4, new Date(product.getExpirationDate().getTime()));
                    return ps;
                },
                keyHolder);
        Long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        product.setId(generatedId);
        return product;
    }

    @Override
    public Product update(Long id, Product product) {
        String sql = "UPDATE product " +
                "SET category_number=?, product_name=?, characteristics=?, expiration_date=? " +
                "WHERE id_product=?";
        jdbcTemplate.update(sql,
                product.getCategoryId(),
                product.getName(),
                product.getCharacteristics(),
                product.getExpirationDate(),
                product.getId());
        return product;
    }

    @Override
    public Optional<Product> deleteById(Long id) {
        Optional<Product> productOptional = findById(id);
        if (productOptional.isPresent()) {
            String sql = "DELETE FROM product WHERE id_product = ?";
            jdbcTemplate.update(sql, id);
        }
        return productOptional;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM product";
        jdbcTemplate.update(sql);
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM product WHERE product_name = ?";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, name);
            return count > 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM product WHERE id_product = ?";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, id);
            return count > 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean existsByNameAndIdIsNot(String name, Long id) {
        String sql = "SELECT COUNT(*) FROM product WHERE product_name = ? AND id_product != ?";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, name, id);
            return count > 0;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
