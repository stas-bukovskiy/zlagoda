package com.zlagoda.sale;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.zlagoda.utils.SortUtils.sortToString;

@Repository
@RequiredArgsConstructor
public class SaleRepositoryImpl implements SaleRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SaleRowMapper rowMapper;

    @Override
    public List<Sale> findAll(Sort sort) {
        String sql = "SELECT * " +
                "FROM sale " +
                "ORDER BY " + sortToString(sort);
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Sale> findById(Pair<String, String> upcAndCheckNumber) {
        String sql = "SELECT * " +
                "FROM sale " +
                "WHERE upc = ? AND check_number = ?";
        List<Sale> sales = jdbcTemplate.query(sql, rowMapper, upcAndCheckNumber.getFirst(), upcAndCheckNumber.getSecond());
        return sales.isEmpty() ? Optional.empty() : Optional.of(sales.get(0));
    }

    @Override
    public Sale save(Sale sale) {
        String sql = "INSERT INTO sale (upc, check_number, product_number, selling_price) " +
                "VALUES (?, ?,?, ?)";
        jdbcTemplate.update(sql,
                sale.getStoreProduct().getUpc(),
                sale.getCheckNumber(),
                sale.getProductNumber(),
                sale.getSellingPrice());
        return sale;
    }

    @Override
    public Sale update(Pair<String, String> upcAndCheckNumber, Sale sale) {
        String sql = "UPDATE sale " +
                "SET product_number=?, selling_price=?" +
                "WHERE upc=? AND check_number=?";
        jdbcTemplate.update(sql,
                sale.getProductNumber(),
                sale.getSellingPrice(),
                upcAndCheckNumber.getFirst(),
                upcAndCheckNumber.getSecond());
        return sale;
    }

    @Override
    public Optional<Sale> deleteById(Pair<String, String> upcAndCheckNumber) {
        Optional<Sale> saleOptional = findById(upcAndCheckNumber);
        if (saleOptional.isPresent()) {
            String sql = "DELETE FROM sale WHERE upc=? AND check_number=?";
            jdbcTemplate.update(sql, upcAndCheckNumber.getFirst(), upcAndCheckNumber.getSecond());
        }
        return saleOptional;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM sale";
        jdbcTemplate.update(sql);
    }

    @Override
    public List<Sale> findAllByCheckNumber(String checkNumber) {
        String sql = "SELECT * " +
                "FROM sale " +
                "WHERE check_number=?";
        return jdbcTemplate.query(sql, rowMapper, checkNumber);
    }

    @Override
    public boolean existsById(Pair<String, String> upcAndCheckNumber) {
        String sql = "SELECT COUNT(*) FROM sale WHERE upc=? AND check_number=?";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, upcAndCheckNumber.getFirst(), upcAndCheckNumber.getSecond());
            return count > 0;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
