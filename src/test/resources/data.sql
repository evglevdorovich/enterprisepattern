INSERT INTO products (ID, name, type)
VALUES (1, 'Product A', 'S'),
       (2, 'Product B', 'W'),
       (3, 'Product C', 'D');

INSERT INTO contracts (ID, product, revenue, date_signed)
VALUES (1, 1, 1000.00, '2024-05-20'), -- date before now
       (2, 2, 1500.00, '2024-05-25'), -- date before now
       (3, 3, 2000.00, '2024-05-27'); -- date after now

INSERT INTO revenue_recognitions (contract, amount, recognized_on)
VALUES (1, 500.00, '2024-05-21'),
       (1, 500.00, '2024-06-21'),
       (2, 750.00, '2024-05-26'),
       (2, 750.00, '2024-06-26'),
       (3, 1000.00, '2024-05-28'),
       (3, 1000.00, '2024-06-28');

INSERT INTO producers (id, name)
VALUES (1, 'Spielberg'),
       (2, 'Tim burton');

INSERT INTO movies (id, name, producer_id)
VALUES (1, 'Jaws', 1),
       (2, 'Bettlejuice', 2);
