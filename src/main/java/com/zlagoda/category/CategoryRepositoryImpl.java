package com.zlagoda.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.zlagoda.utils.SortUtils.sortToString;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CategoryRowMapper rowMapper;

    @Override
    public List<Category> findAll(Sort sort) {
        String sql = "SELECT * " +
                "FROM category " +
                "ORDER BY " + sortToString(sort);
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Category> findById(Long id) {
        String sql = "SELECT * " +
                "FROM category " +
                "WHERE category_number = ?";
        List<Category> categories = jdbcTemplate.query(sql, rowMapper, id);
        return categories.isEmpty() ? Optional.empty() : Optional.of(categories.get(0));
    }

    @Override
    public Category save(Category category) {
        String sql = "INSERT INTO category (category_name) " +
                "VALUES (?)";
        jdbcTemplate.update(sql, category.getName());
        return category;
    }

    @Override
    public Category update(Long id, Category category) {
        String sql = "UPDATE category " +
                "SET category_name=?" +
                "WHERE category_number=?";
        jdbcTemplate.update(sql, category.getName(), category.getId());
        return category;
    }

    @Override
    public Optional<Category> deleteById(Long id) {
        Optional<Category> categoryOptional = findById(id);
        if (categoryOptional.isPresent()) {
            String sql = "DELETE FROM category WHERE category_number = ?";
            jdbcTemplate.update(sql, id);
        }
        return categoryOptional;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM category";
        jdbcTemplate.update(sql);
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) " +
                "FROM category " +
                "WHERE category_name = ?";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, name);
            return count > 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean existsByNameAndIdIsNot(String name, Long id) {
        String sql = "SELECT COUNT(*) " +
                "FROM category " +
                "WHERE category_name = ? AND category_number != ?";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, name, id);
            return count > 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM category WHERE category_number = ?";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, id);
            return count > 0;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
