CREATE SCHEMA x6product;

CREATE TABLE x6product.product (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    price INT NOT NULL
);

CREATE TABLE x6product.meta_product_create (
    id INT NOT NULL PRIMARY KEY REFERENCES x6product.product(id),
    create_date TIMESTAMP WITH TIME ZONE
);

CREATE TABLE x6product.meta_product_update (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES x6product.product(id),
    update_date TIMESTAMP WITH TIME ZONE
);