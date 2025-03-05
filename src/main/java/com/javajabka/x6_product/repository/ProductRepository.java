package com.javajabka.x6_product.repository;

import com.javajabka.x6_product.exception.BadRequestException;
import com.javajabka.x6_product.model.ProductRequest;
import com.javajabka.x6_product.model.ProductResponse;
import com.javajabka.x6_product.repository.maper.ProductMapper;

import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private static final String INSERT = """
            WITH insert_product AS (
                INSERT INTO x6product.product (name, price)
                VALUES (:name, :price)
                RETURNING *
            ), insert_meta_product_creation AS (
                INSERT INTO x6product.meta_product_create (id, create_date)
                (SELECT insert_product.id, now() FROM insert_product)
            )
            SELECT * FROM insert_product;
            """;

    private static final String UPDATE = """
            WITH update_product AS (
                UPDATE x6product.product
                SET name = :name, price = :price
                WHERE id = :id
                RETURNING *
            ), insert_meta_product_update AS (
               INSERT INTO x6product.meta_product_update (user_id, update_date)
               (SELECT update_product.id, now() FROM update_product)
            )
            SELECT * FROM update_product;
            """;

    private static final String EXISTS = """
            SELECT * FROM x6product.product WHERE id IN (:id);
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
        return jdbcTemplate.query(EXISTS, productIdToSql(ids), (rs, rowNum) -> rs.getLong("id"));
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

    private MapSqlParameterSource productIdToSql(final List<Long> productsId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", productsId);
        return params;
    }
}