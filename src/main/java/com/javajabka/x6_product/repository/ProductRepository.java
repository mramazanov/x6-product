package com.javajabka.x6_product.repository;

import com.javajabka.x6_product.exception.BadRequestException;
import com.javajabka.x6_product.model.ProductRequest;
import com.javajabka.x6_product.model.ProductResponse;
import com.javajabka.x6_product.repository.maper.ProductMaper;

import lombok.RequiredArgsConstructor;

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
    private final ProductMaper productMaper;

    public ProductResponse createProduct(final ProductRequest x6ProductRequest) {
        return jdbcTemplate.queryForObject(INSERT, productToSql(null, x6ProductRequest), productMaper);
    }

    public ProductResponse updateProduct(final Long productId, final ProductRequest x6ProductRequest) {
        return jdbcTemplate.queryForObject(UPDATE, productToSql(productId, x6ProductRequest), productMaper);
    }

    public List<Long> existsProduct(final List<Long> ids) {
        return jdbcTemplate.query(EXISTS, productIdToSql(ids), (rs, rowNum) -> rs.getLong("id"));
    }

    public ProductResponse getProductById(Long productId) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, productToSql(productId, null), productMaper);
        } catch (Exception e){
            throw new BadRequestException(String.format("Не удалось найти продукт с id = %d", productId));
        }
    }

    private MapSqlParameterSource productToSql(final Long productId, final ProductRequest x6ProductRequest) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("id", productId);
        if(x6ProductRequest != null) {
            params.addValue("name", x6ProductRequest.getName());
            params.addValue("price", x6ProductRequest.getPrice());
        }

        return params;
    }

    private MapSqlParameterSource productIdToSql(final List<Long> productsId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", productsId);
        return params;
    }
}