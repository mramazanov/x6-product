package com.javajabka.x6_product.repository.maper;

import com.javajabka.x6_product.model.ProductResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductMapper implements RowMapper<ProductResponse> {
    @Override
    public ProductResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ProductResponse.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .price(rs.getBigDecimal("price"))
                .build();
    }
}
