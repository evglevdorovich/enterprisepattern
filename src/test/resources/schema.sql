CREATE TABLE products
(
    ID   int primary key,
    name varchar,
    type varchar
);
CREATE TABLE contracts
(
    ID          int primary key,
    product     int,
    revenue     decimal,
    date_signed date,
    FOREIGN KEY (product) REFERENCES products (id)
);
CREATE TABLE revenue_recognitions
(
    contract      int,
    amount        decimal,
    recognized_on date,
    PRIMARY KEY (contract, recognized_on)
);
CREATE TABLE people
(
    id                   SERIAL PRIMARY KEY,
    first_name           varchar,
    last_name            varchar,
    number_of_dependents int
)
