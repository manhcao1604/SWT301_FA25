-- File: src/test/resources/data.sql - SIMPLIFIED VERSION

-- Only insert test data, no deletion
INSERT INTO users (id, email, password, first_name, last_name, role, created_at) VALUES
                                                                                     (1, 'admin@bookstore.com', 'encoded_password', 'Admin', 'User', 'ADMIN', CURRENT_TIMESTAMP),
                                                                                     (2, 'user@bookstore.com', 'encoded_password', 'John', 'Doe', 'USER', CURRENT_TIMESTAMP);

INSERT INTO books (id, title, author, isbn, price, stock_quantity, description, created_at) VALUES
    (1, 'Spring in Action', 'Craig Walls', '978-1617294945', 39.99, 10, 'Comprehensive guide to Spring Framework', CURRENT_TIMESTAMP);