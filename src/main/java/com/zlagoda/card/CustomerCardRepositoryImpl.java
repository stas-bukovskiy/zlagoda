package com.zlagoda.card;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.zlagoda.utils.SortUtils.sortToString;

@Repository
@RequiredArgsConstructor
public class CustomerCardRepositoryImpl implements CustomerCardRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerCardRowMapper rowMapper;
    private final CustomerCardIdGenerator idGenerator;

    @Override
    public List<CustomerCard> findAll(Sort sort) {
        String sql = """
                SELECT *
                FROM customer_card
                ORDER BY ?
                """;
        return jdbcTemplate.query(sql, rowMapper, sortToString(sort));
    }

    @Override
    public Optional<CustomerCard> findById(String id) {
        String sql = """
                SELECT *
                FROM customer_card
                WHERE card_number = ?
                """;
        List<CustomerCard> cards = jdbcTemplate.query(sql, rowMapper, id);
        return cards.isEmpty() ? Optional.empty() : Optional.of(cards.get(0));
    }

    @Override
    public CustomerCard save(CustomerCard card) {
        card.setCardNumber(idGenerator.generate());
        String sql = """
                INSERT INTO customer_card (card_number,
                                           cust_surname,
                                           cust_name,
                                           cust_patronymic,
                                           phone_number,
                                           city,
                                           street,
                                           zip_code,
                                           percent)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, card.getCardNumber(), card.getSurname(), card.getName(), card.getPatronymic(), card.getPhoneNumber(), card.getCity(), card.getStreet(), card.getZipCode(), card.getPercent());
        return card;
    }

    @Override
    public CustomerCard update(String id, CustomerCard card) {
        String sql = """
                UPDATE customer_card
                SET cust_surname=?,
                    cust_name=?,
                    cust_patronymic=?,
                    phone_number=?,
                    city=?,
                    street=?,
                    zip_code=?,
                    percent=?
                WHERE card_number=?
                                """;
        jdbcTemplate.update(sql, card.getSurname(), card.getName(), card.getPatronymic(), card.getPhoneNumber(), card.getCity(), card.getStreet(), card.getZipCode(), card.getPercent(), card.getCardNumber());
        return card;
    }

    @Override
    public Optional<CustomerCard> deleteById(String id) {
        Optional<CustomerCard> cardOptional = findById(id);
        if (cardOptional.isPresent()) {
            String sql = """
                    DELETE
                    FROM customer_card
                    WHERE card_number = ?
                    """;
            jdbcTemplate.update(sql, id);
        }
        return cardOptional;
    }

    @Override
    public void deleteAll() {
        String sql = """
                DELETE
                FROM customer_card
                """;
        jdbcTemplate.update(sql);
    }

    @Override
    public List<String> findAllDistinctCities() {
        String sql = """
                SELECT DISTINCT city
                FROM customer_card
                """;
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<String> findAllDistinctStreets() {
        String sql = """
                SELECT DISTINCT street
                FROM customer_card
                """;
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<String> findAllDistinctZipCodes() {
        String sql = """
                SELECT DISTINCT zip_code
                FROM customer_card
                """;
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public boolean existsById(String id) {
        String sql = """
                SELECT COUNT(*)
                FROM customer_card
                WHERE card_number = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (count != null)
            return count > 0;
        return false;
    }
}
