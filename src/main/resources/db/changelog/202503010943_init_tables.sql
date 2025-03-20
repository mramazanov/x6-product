CREATE SCHEMA x6product;

CREATE TABLE x6product.product (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    price INT NOT NULL,
    create_date TIMESTAMP WITH TIME ZONE,
    update_date TIMESTAMP WITH TIME ZONE
);