package com.javajabka.x6_product.repository;

import com.javajabka.x6_product.exception.BadRequestException;
import com.javajabka.x6_product.model.ProductRequest;
import com.javajabka.x6_product.model.ProductResponse;
import com.javajabka.x6_product.repository.maper.ProductMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private static final String INSERT = """
            INSERT INTO x6product.product (name, price, create_date)
            VALUES (:name, :price, now())
            RETURNING *
            """;

    private static final String UPDATE = """
           UPDATE x6product.product
           SET name = :name, price = :price, update_date = now()
           WHERE id = :id
           RETURNING *
           """;

    private static final String EXISTS = """
            SELECT id FROM x6product.product WHERE id IN (:id);
            """;

    private static final String GET_BY_ID = """
            SELECT * FROM x6product.product WHERE id = :id;
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ProductMapper productMapper;

    public ProductResponse insert(final ProductRequest productRequest) {
        return jdbcTemplate.queryForObject(INSERT, productToSql(null, productRequest), productMapper);
    }

    public ProductResponse update(final Long id, final ProductRequest productRequest) {
        try {
            return jdbcTemplate.queryForObject(UPDATE, productToSql(id, productRequest), productMapper);
        } catch (final EmptyResultDataAccessException e) {
            throw new BadRequestException(String.format("Не удалось найти продукт с id = %d", id));
        }
    }

    public List<Long> exist(final List<Long> ids) {
        return jdbcTemplate.query(EXISTS, idsToSql(ids), (rs, rowNum) -> rs.getLong("id"));
    }

    public ProductResponse getProductById(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, productToSql(id, null), productMapper);
        } catch (Exception e){
            throw new BadRequestException(String.format("Не удалось найти продукт с id = %d", id));
        }
    }

    private MapSqlParameterSource productToSql(final Long id, final ProductRequest productRequest) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("id", id);
        if(productRequest != null) {
            params.addValue("name", productRequest.getName());
            params.addValue("price", productRequest.getPrice());
        }

        return params;
    }

    private MapSqlParameterSource idsToSql(final List<Long> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", ids);
        return params;
    }
}